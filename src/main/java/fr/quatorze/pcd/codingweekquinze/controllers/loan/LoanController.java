package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.controllers.MessageController;
import fr.quatorze.pcd.codingweekquinze.dao.NotificationDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class LoanController {

    private final Loan loan;

    @FXML
    private BorderPane root;

    @FXML
    private HBox actionSection;
    private Button acceptButton;
    private Button cancelButton;
    private Button finishButton;

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
    private Label borrower;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    public LoanController(Loan loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        FXMLLoaderUtil.inject("message.fxml", new MessageController(loan), node -> {
            root.setRight(node);
        });

        this.title.setText("Prêt de " + loan.getItem().getName());
        if (loan.getItem().getImage() != null) {
            this.image.setImage(new Image(loan.getItem().getImage()));
        }

        this.itemType.setText("Type : " + (loan.getItem().getIsService() ? "Service" : "Objet"));
        this.itemPrice.setText("Prix : " + loan.getItem().getPrice() + "€");
        this.itemName.setText("Nom : " + loan.getItem().getName());
        this.borrower.setText(loan.getBorrower().getFirstName() + " " + loan.getBorrower().getLastName());
        this.startDate.setText(loan.getStartDate().toString());
        this.endDate.setText(loan.getEndDate().toString());


        if (this.loan.getStatus() == 0) {
            this.acceptButton = new Button("Accepter");
            acceptButton.setOnAction(event -> accept());
            acceptButton.getStyleClass().add(0, "btn-primary");
            actionSection.getChildren().add(acceptButton);
            this.cancelButton = new Button("Annuler");
            cancelButton.setOnAction(event -> cancel());
            cancelButton.getStyleClass().add("btn-cancel");
            actionSection.getChildren().add(1, cancelButton);
        } else if (this.loan.getStatus() == 1) {
            this.finishButton = new Button("Terminer");
            finishButton.setOnAction(event -> finish());
            finishButton.getStyleClass().add("btn-primary");
            actionSection.getChildren().add(0, finishButton);
        }
    }

    @FXML
    private void accept() {
        loan.accept();
        acceptButton.setVisible(false);
        cancelButton.setVisible(false);
        this.finishButton = new Button("Terminer");
        finishButton.getStyleClass().add("btn-primary");
        finishButton.setOnAction(event -> finish());
        actionSection.getChildren().add(0, finishButton);
    }

    @FXML
    public void cancel() {
        loan.cancel();
        acceptButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    @FXML
    private void finish() {
        User authUser = AuthService.getInstance().getCurrentUser();
        try {
            if (authUser.equals(loan.getItem().getOwner())) {
                User borrower = loan.getBorrower();
                borrower.pay(authUser, loan.getItem().getPrice());
                loan.awaitRating();
                finishButton.setVisible(false);

                if (loan.getItem().getIsService()) {
                    NotificationDAO.getInstance().createNotification(borrower, "Le service " + loan.getItem().getName() + " a été rendu, allez notez dans les vos emprunts.");
                } else {
                    NotificationDAO.getInstance().createNotification(borrower, "L'objet " + loan.getItem().getName() + " a été rendu, allez notez dans les vos emprunts.");
                }
            } else {
                LayoutManager.alert("Vous n'êtes pas le propriétaire de cet objet");
            }
        } catch (IllegalArgumentException e) {
            LayoutManager.alert("L'emprunteur n'a pas assez d'argent");
        }
    }

    public void askAdminHelp(MouseEvent mouseEvent) {
        LayoutManager.info("L'administrateur a été prévenu");

        for (User allAdmin : UserDAO.getInstance().getAllAdmins()) {
            NotificationDAO.getInstance().createNotification(allAdmin, "L'utilisateur " + AuthService.getInstance().getCurrentUser().getFirstName() + " " + AuthService.getInstance().getCurrentUser().getLastName() + " a besoin d'aide");
        }
    }
}
