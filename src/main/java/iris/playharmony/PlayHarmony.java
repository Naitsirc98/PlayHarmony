package iris.playharmony;


import iris.playharmony.controller.NavController;
import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.util.MediaFactory;
import iris.playharmony.view.MainView;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.song.UserSongListView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class PlayHarmony extends Application {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    public static final String TITLE = "Play Harmony";

    private static final AtomicReference<PlayHarmony> INSTANCE = new AtomicReference<>();

    public static PlayHarmony get() {
        return INSTANCE.get();
    }

    private Stage window;
    private Scene scene;

    public PlayHarmony() {
        if(!INSTANCE.compareAndSet(null, this)) {
            throw new ExceptionInInitializerError("PlayHarmony has already been created");
        }
    }

    public Stage getWindow() {
        return window;
    }

    public Scene getScene() {
        return scene;
    }

    public synchronized void start(Stage primaryStage) {

        if(window != null) {
            throw new IllegalStateException("Play Harmony has already started");
        }

        window = primaryStage;

        primaryStage.setTitle(TITLE);

        createScene();

        primaryStage.setScene(scene);

        primaryStage.show();

        // NavController.get().setView(new UserSongListView());

        showMusicPlayerView();
    }

    private void showMusicPlayerView() {

        MusicPlayer musicPlayer = new MusicPlayer();

        Song song = new Song();

        song.setPathFile("G:\\Eclipse\\JWorkspace\\MusicPlayerFX\\Slippy - Promise Me [Monstercat Release].mp3");

        MusicPlayerViewModel viewModel = new MusicPlayerViewModel(musicPlayer);

        MusicPlayerView view = new MusicPlayerView(viewModel);

        NavController.get().setView(view);

        // viewModel.setSong(song);

        musicPlayer.setSong(MediaFactory.getMedia(song.getPathFile()));

        musicPlayer.play();
    }

    private void createScene() {
        scene = new Scene(new MainView(), DEFAULT_WIDTH, DEFAULT_HEIGHT, false, SceneAntialiasing.BALANCED);
    }
}
