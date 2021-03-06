package iris.playharmony.model;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class User {

    private File photo;
    private String name;
    private String surname;
    private String category;
    private Role role;
    private Email email;
    private List<Playlist> playLists;
    private String password;
    private Playlist favorites = new Playlist("favourites");

    public User() {}

    public User(File photo, String name, String surname, String category, Role role, Email email, List<Playlist> playLists) {
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.category = category;
        this.role = role;
        this.email = email;
        this.playLists = playLists;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setFavorites(Playlist favorites) {
        this.favorites = favorites;
    }

    public File getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCategory() {
        return category;
    }

    public Role getRole() {
        return role;
    }

    public Email getEmail() {
        return email;
    }

    public User photo(File photo) {
        this.photo = photo;
        return this;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public User surname(String surname) {
        this.surname = surname;
        return this;
    }

    public User category(String category) {
        this.category = category;
        return this;
    }

    public User role(Role role) {
        this.role = role;
        return this;
    }

    public User mail(Email email) {
        this.email = email;
        return this;
    }

    public User favourites(Playlist playlist) {
        this.favorites = playlist;
        return this;
    }

    public Playlist favourites() {
        return this.favorites;
    }

    public List<Playlist> getPlayLists() {
        return playLists;
    }

    public void addPlayList(Playlist playlist){
        playLists.add(playlist);
    }

    public User setPlayLists(List<Playlist> playLists) {
        this.playLists = playLists;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "photo=" + photo +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", category='" + category + '\'' +
                ", role=" + role +
                ", email=" + email +
                ", playLists=" + playLists +
                ", password='" + password + '\'' +
                '}';
    }
}
