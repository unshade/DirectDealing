package fr._14.pcd.codingweek15.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "users")
@Getter
@Setter
public class User {
  @DatabaseField(generatedId = true)
  private int id;

  @DatabaseField
  private String name;

}
