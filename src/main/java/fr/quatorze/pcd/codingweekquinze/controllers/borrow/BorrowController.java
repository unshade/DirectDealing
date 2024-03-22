package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.controllers.MessageController;
import fr.quatorze.pcd.codingweekquinze.dao.NotificationDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.enums.LoanStatus;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.util.DateUtil;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class BorrowController {

    private final Loan loan;

    @FXML
    private BorderPane root;

    @FXML
    private HBox actionSection;

    @FXML
    private Label title;
    @FXML
    public ImageView image;
    @FXML
    private Label itemType;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemName;
    @FXML
    private Label owner;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    private MFXFontIcon[] icons;
    private MFXButton rateButton;

    private int rating;

    public BorrowController(Loan loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        FXMLLoaderUtil.inject("message.fxml", new MessageController(loan), node -> {
            root.setRight(node);
        });

        this.title.setText("Emprunt de " + loan.getItem().getName());
        if (loan.getItem().getImage() != null) {
            this.image.setImage(new Image(loan.getItem().getImage()));
        }   

        this.itemType.setText("Type : " + (loan.getItem().getIsService() ? "Service" : "Objet"));
        this.itemPrice.setText("Prix : " + loan.getItem().getPrice() + "⚘");
        this.itemName.setText("Nom : " + loan.getItem().getName());
        this.owner.setText(loan.getItem().getOwner().getFirstName() + " " + loan.getItem().getOwner().getLastName());
        this.startDate.setText(DateUtil.format(loan.getStartDate()));
        this.endDate.setText(DateUtil.format(loan.getEndDate()));

        if (this.loan.getStatus() == LoanStatus.AWAITING_RATING.ordinal()) {
            this.icons = new MFXFontIcon[5];
            for (int i = 0; i < 5; i++) {
                icons[i] = new MFXFontIcon("fas-star", 32, Color.LIGHTGRAY);
                int finalI = i;
                icons[i].setOnMouseEntered(event -> {
                    for (int j = 0; j < finalI + 1; j++) {
                        icons[j].setColor(Color.LIGHTGRAY);
                    }
                    for (int j = finalI; j < 5; j++) {
                        icons[j].setColor(Color.GOLD);
                    }
                    rating = finalI + 1;
                });
                actionSection.getChildren().add(0, icons[i]);
            }

            this.rateButton = new MFXButton("Noter");
            rateButton.setOnAction(event -> rate());
            rateButton.getStyleClass().add("btn-primary");
            actionSection.getChildren().add(actionSection.getChildren().size() - 1, rateButton);
        }
    }

    private void rate() {
        rateButton.setVisible(false);
        loan.setRating(rating);
        loan.end();
        LayoutManager.info("Merci d'avoir noté l'emprunt");
    }

    public void askAdminHelp(MouseEvent mouseEvent) {
        LayoutManager.info("L'administrateur a été prévenu");

        for (User allAdmin : UserDAO.getInstance().getAllAdmins()) {
            NotificationDAO.getInstance().createNotification(allAdmin, "L'utilisateur " + AuthService.getInstance().getCurrentUser().getFirstName() + " " + AuthService.getInstance().getCurrentUser().getLastName() + " a besoin d'aide");
        }
    }
}
