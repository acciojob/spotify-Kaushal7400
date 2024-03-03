package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {

        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album = new Album(title);
        albums.add(album);
        for(Artist artist:artists){
            if(artist.equals(artistName)) {
                artistAlbumMap.put(artist,albums);
                return album;
            }
        }
        Artist artist = new Artist(artistName);
        artistAlbumMap.put(artist,albums);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title,length);
        songs.add(song);
        for(Album album:albums){
            if(album.equals(albumName)) {
                albumSongMap.put(album,songs);
                return song;
            }
        }
        albumSongMap.put(new Album(albumName),songs);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = new Playlist(title);
        List<User> users1 = new ArrayList<>();
        List<Song> songs1 = new ArrayList<>();
        for(Song song:songs) {
            if(length == song.getLength()) {
               songs1.add(song);
            }
        }
        playlistSongMap.put(playlist,songs1);


        for(User user:users) {
            if(mobile.equals(user.getMobile())) {
                creatorPlaylistMap.put(user, playlist);
//                Listener
                users1.add(user);
                break;
            }
        }
        playlistListenerMap.put(playlist,users1);
        playlists.add(playlist);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);
        List<User> users1 = new ArrayList<>();
        List<Song> songs1 = new ArrayList<>();
        for(String ti:songTitles) {
            for(Song s:songs) {
                if(s.getTitle().equals(ti)) {
                    songs1.add(s);
                }
            }
        }
        playlistSongMap.put(playlist,songs1);

        for(User user:users) {
            if(mobile.equals(user.getMobile())) {
                creatorPlaylistMap.put(user, playlist);
//                Listener
                users1.add(user);
                break;
            }
        }
        playlistListenerMap.put(playlist,users1);
        playlists.add(playlist);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User ansUser = findUser(mobile);

        Playlist ansPlaylist = null;
        for(Playlist p:playlists) {
            if(p.getTitle().equals(playlistTitle)) {
                List<User> users1 = playlistListenerMap.get(p);
                if(!users1.contains(ansUser)) {
                    users1.add(ansUser);
                    ansPlaylist = p;
                }
            }
        }
        return ansPlaylist;
    }
//  /////////////  Find User///////////////////
    public User findUser(String mobile) {
        for(User user:users) {
            if(user.getMobile().equals(mobile)) return user;
        }
        return null;
    }
    public Song likeSong(String mobile, String songTitle) throws Exception {
        User ansUser = findUser(mobile);

        for(Song song:songs) {
            if(song.getTitle().equals(songTitle)) {
                List<User> userList = songLikeMap.get(song);
                if(!userList.contains(ansUser)) {
                    userList.add(ansUser);
                    int like = song.getLikes();
                    song.setLikes(like+1);
                    Album album = findAlbum(song);
                    Artist artist = findArtist(album);
                    artist.setLikes(artist.getLikes()+1);
                    return song;
                }

            }
        }
        return null;

    }
//  ////////////  finding album using song
    public Album findAlbum(Song song) {
        for(Album album:albumSongMap.keySet()) {
            List<Song> list = albumSongMap.get(album);
            if(list.contains(song)) return album;
        }
        return null;
    }

 /////////////   // Finding Artist using album
    public Artist findArtist(Album album) {
        for(Artist artist:artistAlbumMap.keySet()) {
            List<Album> list = artistAlbumMap.get(artist);
            if(list.contains(album)) return artist;
        }
        return null;
    }
    public String mostPopularArtist() {
        int maxLikes = 0;
        String ans = "";
        for(Artist artist:artists) {
            if(maxLikes < artist.getLikes()) {
                maxLikes = artist.getLikes();
                ans = artist.getName();
            }
        }
        return ans;
    }

    public String mostPopularSong() {
        int maxLikes = 0;
        String ans = "";
        for(Song song:songs) {
            if(maxLikes < song.getLikes()) {
                maxLikes = song.getLikes();
                ans = song.getTitle();
            }
        }
        return ans;
    }
}
