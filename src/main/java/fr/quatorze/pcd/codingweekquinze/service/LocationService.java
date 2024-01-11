package fr.quatorze.pcd.codingweekquinze.service;

import fr.quatorze.pcd.codingweekquinze.MainApplication;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

public final class LocationService {

    private static LocationService instance;

    public static LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    public void initDatabase(Session session) throws FileNotFoundException {
        session.beginTransaction();
        String query = """
                CREATE TABLE IF NOT EXISTS commune_data (
                    code_commune_INSEE INT,
                    nom_commune_postal VARCHAR(255),
                    code_postal VARCHAR(20),\s
                    libelle_acheminement VARCHAR(255),
                    ligne_5 VARCHAR(255),
                    latitude FLOAT,
                    longitude FLOAT,
                    code_commune INT,
                    article VARCHAR(255),
                    nom_commune VARCHAR(255),
                    nom_commune_complet VARCHAR(255),
                    code_departement VARCHAR(20),\s
                    nom_departement VARCHAR(255),
                    code_region VARCHAR(20),\s
                    nom_region VARCHAR(255)
                );
                """;
        session.createNativeQuery(query).executeUpdate();
        session.getTransaction().commit();
        session.close();

        File file = new File(MainApplication.class.getResource("city/communes-departement-region.csv").getFile());
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            // remove first line containing column names
            lines.remove(0);

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            for (String line : lines) {
                String[] data = new String[15];
                Arrays.fill(data, "");
                int i = 0;
                for (Character c : line.toCharArray()) {
                    if (c == ',') {
                        i++;
                    } else {
                        data[i] += c;
                    }
                }

                query = """
                        INSERT INTO commune_data (
                            code_commune_INSEE,
                            nom_commune_postal,
                            code_postal,
                            libelle_acheminement,
                            ligne_5,
                            latitude,
                            longitude,
                            code_commune,
                            article,
                            nom_commune,
                            nom_commune_complet,
                            code_departement,
                            nom_departement,
                            code_region,
                            nom_region
                        ) VALUES (
                            :code_commune_INSEE,
                            :nom_commune_postal,
                            :code_postal,
                            :libelle_acheminement,
                            :ligne_5,
                            :latitude,
                            :longitude,
                            :code_commune,
                            :article,
                            :nom_commune,
                            :nom_commune_complet,
                            :code_departement,
                            :nom_departement,
                            :code_region,
                            :nom_region
                        );
                        """;
                session.createNativeQuery(query)
                        .setParameter("code_commune_INSEE", data[0])
                        .setParameter("nom_commune_postal", data[1])
                        .setParameter("code_postal", data[2])
                        .setParameter("libelle_acheminement", data[3])
                        .setParameter("ligne_5", data[4])
                        .setParameter("latitude", data[5])
                        .setParameter("longitude", data[6])
                        .setParameter("code_commune", data[7])
                        .setParameter("article", data[8])
                        .setParameter("nom_commune", data[9])
                        .setParameter("nom_commune_complet", data[10])
                        .setParameter("code_departement", data[11])
                        .setParameter("nom_departement", data[12])
                        .setParameter("code_region", data[13])
                        .setParameter("nom_region", data[14])
                        .executeUpdate();
            }
            session.getTransaction().commit();
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the list of cities with names starting with the given prefix.
     * The search does not care about accents or case.
     *
     * @param prefix the prefix
     * @return the list of cities name starting with the given prefix, spelled exactly as they should be (with accents...)
     */
    public List<String> getCitiesStartingWith(String prefix) {
        // Normalize for accents
        prefix = Normalizer.normalize(prefix, Normalizer.Form.NFKD);
        prefix = prefix.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String query = """
                SELECT DISTINCT nom_commune FROM commune_data WHERE replace(replace(nom_commune_postal, ' ', '-'), '!', '') LIKE :prefix
                """;
        List<String> cities = session.createNativeQuery(query)
                .setParameter("prefix", prefix + "%")
                .getResultList();
        session.getTransaction().commit();
        session.close();

        return cities;
    }

    /**
     * Get the list of names of cities near the provided city name.
     * The provided city name must be exactly the one in database.
     *
     * @param city       the city name
     * @param kmDistance the max distance to this city
     * @return the list of city names near the given city
     */
    public List<String> getCitiesNear(String city, float kmDistance) {
        float latitude, longitude;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String query = """
                SELECT latitude, longitude FROM commune_data WHERE nom_commune = :city group by nom_commune
                """;
        List<Object[]> result = session.createNativeQuery(query)
                .setParameter("city", city)
                .getResultList();
        session.getTransaction().commit();
        session.close();

        if (result.size() == 1) {
            latitude = (float) result.get(0)[0];
            longitude = (float) result.get(0)[1];
        } else {
            return List.of(city);
        }

        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        query = """
                SELECT DISTINCT nom_commune FROM commune_data WHERE
                acos(sin(latitude * 0.0174533) * sin(:latitude * 0.0174533) + cos(latitude * 0.0174533) * cos(:latitude * 0.0174533) * cos(longitude * 0.0174533 - :longitude * 0.0174533)) * 6378.1 <= :kmDistance
                ORDER BY acos(sin(latitude * 0.0174533) * sin(:latitude * 0.0174533) + cos(latitude * 0.0174533) * cos(:latitude * 0.0174533) * cos(longitude * 0.0174533 - :longitude * 0.0174533)) * 6378.1
                """;
        List<String> cities = session.createNativeQuery(query)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("kmDistance", kmDistance)
                .getResultList();
        session.getTransaction().commit();
        session.close();

        return cities;
    }

    public boolean doesCityExist(String cityName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String query = """
                SELECT COUNT(*) FROM commune_data WHERE nom_commune = :cityName
                """;
        int count = (int) session.createNativeQuery(query)
                .setParameter("cityName", cityName)
                .getSingleResult();
        session.getTransaction().commit();
        session.close();

        return count > 0;
    }
}
