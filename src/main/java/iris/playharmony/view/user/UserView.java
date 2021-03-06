package iris.playharmony.view.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.session.EditAccountView;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.playlist.CreatePlaylistView;
import iris.playharmony.view.user.playlist.FavouriteSongListView;
import iris.playharmony.view.user.playlist.PlaylistView;
import iris.playharmony.view.user.playlist.UpdatePlaylistView;
import iris.playharmony.view.user.song.UserSongListView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class UserView extends ListTemplate<Playlist> {

    public UserView() {
        super("Select Playlist");
    }

    @Override
    protected Comparator<Playlist> initComparator() {
        return Comparator.comparing(Playlist::getName);
    }

    @Override
    protected ObservableList<Playlist> getData() {

        ObservableList<Playlist> playlists = FXCollections.observableArrayList();

        User user = Session.getSession().currentUser();

        if(user == null) {
            return FXCollections.emptyObservableList();
        }

        if(user.getPlayLists() != null) {
            playlists.addAll(user.getPlayLists());
        }

        return playlists;
    }

    @Override
    protected String fieldToFilter(Playlist playlist) {
        return playlist.getName();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Search Songs", e -> NavController.get().pushView(new UserSongListView())),
                ButtonFactory.button("Add Playlist", e -> NavController.get().pushView(new CreatePlaylistView())),
                ButtonFactory.button("Delete Playlist", event -> deletePlaylist()),
                ButtonFactory.button("Update Playlist", event -> updatePlaylist()),
                ButtonFactory.button("Edit Account", event -> editAccount()),
                ButtonFactory.button("See Playlist", event -> seePlaylist()),
                ButtonFactory.button("See Favourites", event -> seeFavourites())
        };
    }

    private void seePlaylist() {
        Playlist selectedItem = getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new PlaylistView(selectedItem));
        }
    }

    private void deletePlaylist() {
        Playlist selectedItem = getSelectedItem();
        if(AlertFactory.confirmAlert("Confirm", "Are you sure you want to delete the playlist?")){
            DatabaseController.get().deletePlayList(selectedItem, Session.getSession().currentUser());
            refresh();
        }
    }

    private void updatePlaylist(){
        Playlist selectedItem = getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new UpdatePlaylistView(selectedItem));
        }
    }

    private void editAccount(){
        NavController.get().pushView(new EditAccountView());
    }

    private void seeFavourites() {
        NavController.get().pushView(new FavouriteSongListView());
    }
}
