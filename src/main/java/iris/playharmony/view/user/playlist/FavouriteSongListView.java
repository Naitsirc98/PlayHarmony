package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.model.User;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Comparator;

public class FavouriteSongListView extends ListTemplate {

    private TextField searchField;
    private ObservableList<ObservableSong> data;
    private Comparator<ObservableSong> comparator;

    public FavouriteSongListView() {
        super("User favorite songs");
    }

    @Override
    protected void initElements() {
        comparator = Comparator.comparing(observable -> observable.title().get());
        data = getObservableData();
        searchField = new TextField();
    }

    @Override
    protected void initSearchForm() {
        HBox searchRow = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        searchField.setOnAction(event -> searchCommand());
        searchRow.getChildren().add(region);
        searchRow.getChildren().add(searchField);
        searchRow.getChildren().add(ButtonFactory.button("Search", event -> searchCommand()));

        add(searchRow);
    }

    @Override
    protected void initTable() {
        add(table = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));
    }

    @Override
    protected void initPagination() {
        add(pagination = TableFactory.pagination(data, table));
    }

    @Override
    protected ObservableList<ObservableSong> getObservableData() {
        data = FXCollections.observableArrayList();
        User user = Session.getSession().currentUser();
        if(Session.getSession().currentUser().favourites() == null)
            return FXCollections.emptyObservableList();
        Session.getSession().currentUser().favourites()
                .getSongList()
                .stream()
                .map(ObservableSong::from)
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Play all", this::playAll),
                ButtonFactory.button("Play song", this::playSong),
                ButtonFactory.button("Delete song", event -> deleteSong())
        };
    }

    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void searchCommand() {
        refresh();

        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void playAll(ActionEvent actionEvent) {
        actionEvent.consume();
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        Song song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }

    private void deleteSong() {
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song selectedSong = new DatabaseController().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                User currentUser = Session.getSession().currentUser();
                currentUser.favourites().deleteSong(selectedSong);
                new DatabaseController().addFavourites(currentUser.favourites(),
                        currentUser);
                refresh();
            }
        }
    }
}