package iris.playharmony.view.main;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.util.ButtonFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class LobbyView extends VBox {

    private static int SPACING = 15;

    public LobbyView() {
        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(ButtonFactory.button("Admin", event -> NavController.get().pushView(new AdminView())));
        add(ButtonFactory.button("User", event -> NavController.get().pushView(new UserView())));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }
}