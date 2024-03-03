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
        for(User u: users){
            if(mobile.equals(u.getMobile())) return u;
        }
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        for(Artist a: artists){
            if(name.equals(a.getName())) return a;
        }

        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
//        Album album = new Album(title);
//        albums.add(album);
//        for(Artist artist:artists){
//            if(artist.equals(artistName)) {
//                artistAlbumMap.put(artist,albums);
//                return album;
//            }
//        }
//        Artist artist = new Artist(artistName);
//        artistAlbumMap.put(artist,albums);
//        return album;

        // if artists does not exists
        Artist artistKey = createArtist(artistName);

        // creating album
        for(Album album : albums){
            if(album.getTitle().equals(title))
                return  album;
        }
        Album tempAlbum = new Album(title);
        albums.add(tempAlbum);

        // put in artist-album map
        List<Album> tempAlbumList = artistAlbumMap.getOrDefault(artistKey,new ArrayList<>());
        tempAlbumList.add(tempAlbum);
        artistAlbumMap.put(artistKey,tempAlbumList);

        return tempAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
//        Song song = new Song(title,length);
//        songs.add(song);
//        for(Album album:albums){
//            if(album.equals(albumName)) {
//                albumSongMap.put(album,songs);
//                return song;
//            }
//        }
//        albumSongMap.put(new Album(albumName),songs);
//        return song;
        // search for album
        Album albumKey = null;
        for(Album a: albums){
            if(albumName.equals(a.getTitle())){
                albumKey = a;
                break;
            }
        }
        if(albumKey==null)   throw new Exception("Album does not exist");

        // create song
        Song gaana = new Song(title,length);
        songs.add(gaana);

        // putting in album - song map
        List<Song> tempSongsList = albumSongMap.getOrDefault(albumKey,new ArrayList<>());
        tempSongsList.add(gaana);
        albumSongMap.put(albumKey,tempSongsList);

        return gaana;
    }

    public User getUser(String mobile){
        User currUser = null;
        for(User a: users){
            if(mobile.equals(a.getMobile())){
                currUser = a;
                break;
            }
        }
        return currUser;
    }

//    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
//        Playlist playlist = new Playlist(title);
//        List<User> users1 = new ArrayList<>();
//        List<Song> songs1 = new ArrayList<>();
//        for(Song song:songs) {
//            if(length == song.getLength()) {
//               songs1.add(song);
//            }
//        }
//        playlistSongMap.put(playlist,songs1);
//
//
//        for(User user:users) {
//            if(mobile.equals(user.getMobile())) {
//                creatorPlaylistMap.put(user, playlist);
////                Listener
//                users1.add(user);
//                break;
//            }
//        }
//        playlistListenerMap.put(playlist,users1);
//        playlists.add(playlist);
//        return playlist;
//
//    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist p: playlists){
            if(title.equals(p.getTitle())){
                return p;
            }
        }
        Playlist tempPlaylist = new Playlist(title);
        playlists.add(tempPlaylist);


        // list of songs having given length
        List<Song> songOfGivenLength = new ArrayList<>();
        for(Song s: songs){
            if(length == s.getLength()){
                songOfGivenLength.add(s);
            }
        }

        playlistSongMap.put(tempPlaylist,songOfGivenLength);


        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        List<User> listOfListener = playlistListenerMap.getOrDefault(tempPlaylist,new ArrayList<>());
        listOfListener.add(currUser);
        playlistListenerMap.put(tempPlaylist,listOfListener);


        creatorPlaylistMap.put(currUser,tempPlaylist);


        List<Playlist> listOfPlaylist = userPlaylistMap.getOrDefault(currUser,new ArrayList<>());
        listOfPlaylist.add(tempPlaylist);
        userPlaylistMap.put(currUser,listOfPlaylist);

        return tempPlaylist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
//        Playlist playlist = new Playlist(title);
//        List<User> users1 = new ArrayList<>();
//        List<Song> songs1 = new ArrayList<>();
//        for(String ti:songTitles) {
//            for(Song s:songs) {
//                if(s.getTitle().equals(ti)) {
//                    songs1.add(s);
//                }
//            }
//        }
//        playlistSongMap.put(playlist,songs1);
//
//        for(User user:users) {
//            if(mobile.equals(user.getMobile())) {
//                creatorPlaylistMap.put(user, playlist);
////                Listener
//                users1.add(user);
//                break;
//            }
//        }
//        playlistListenerMap.put(playlist,users1);
//        playlists.add(playlist);
//        return playlist;


        for(Playlist p: playlists){
            if(title.equals(p.getTitle())){
                return p;
            }
        }
        // playlist
        Playlist tempPlaylist = new Playlist(title);
        playlists.add(tempPlaylist);

        // songs
        List<Song> songOfGivenName = new ArrayList<>();
        for(Song s: songs){
            if(songTitles.contains(s.getTitle())){
                songOfGivenName.add(s);
            }
        }

        // playlist - list of song map
        playlistSongMap.put(tempPlaylist,songOfGivenName);

        // user
        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        // playlist - list of listeners map
        List<User> listOfListener = playlistListenerMap.getOrDefault(tempPlaylist,new ArrayList<>());
        listOfListener.add(currUser);
        playlistListenerMap.put(tempPlaylist,listOfListener);

        // creator - playlist map
        creatorPlaylistMap.put(currUser,tempPlaylist);

        // user - list of playlist map
        List<Playlist> listOfPlaylist = userPlaylistMap.getOrDefault(currUser,new ArrayList<>());
        listOfPlaylist.add(tempPlaylist);
        userPlaylistMap.put(currUser,listOfPlaylist);

        return tempPlaylist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
//        User ansUser = findUser(mobile);
//
//        Playlist ansPlaylist = null;
//        for(Playlist p:playlists) {
//            if(p.getTitle().equals(playlistTitle)) {
//                List<User> users1 = playlistListenerMap.get(p);
//                if(!users1.contains(ansUser)) {
//                    users1.add(ansUser);
//                    ansPlaylist = p;
//                }
//            }
//        }
//        return ansPlaylist;


        // check for user existance
        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        // check for playlist existance
        Playlist currPlaylist = null;
        for(Playlist p: playlists){
            if(p.getTitle().equals(playlistTitle)){
                currPlaylist = p;
                break;
            }
        }
        if(currPlaylist==null) throw new Exception("Playlist does not exist");



        // listener playlist
        List<User> tempList = playlistListenerMap.getOrDefault(currPlaylist,new ArrayList<>());
        if(!tempList.contains(currUser)){
            tempList.add(currUser);
            playlistListenerMap.put(currPlaylist,tempList);
        }


        // creator playlist
//        if(!creatorPlaylistMap.get(currUser).equals(currPlaylist)){
//            creatorPlaylistMap.put(currUser,currPlaylist);
//        }

        // user playlist
        List<Playlist> temp2PlayList =  userPlaylistMap.getOrDefault(currUser,new ArrayList<>());
        if(!temp2PlayList.contains(currPlaylist)){
            temp2PlayList.add(currPlaylist);
            userPlaylistMap.put(currUser,temp2PlayList);
        }


        return currPlaylist;

    }
//  /////////////  Find User///////////////////
//    public User findUser(String mobile) {
//        for(User user:users) {
//            if(user.getMobile().equals(mobile)) return user;
//        }
//        return null;
//    }
    public Song likeSong(String mobile, String songTitle) throws Exception {
//        User ansUser = findUser(mobile);
//
//        for(Song song:songs) {
//            if(song.getTitle().equals(songTitle)) {
//                List<User> userList = songLikeMap.get(song);
//                if(!userList.contains(ansUser)) {
//                    userList.add(ansUser);
//                    int like = song.getLikes();
//                    song.setLikes(like+1);
//                    Album album = findAlbum(song);
//                    Artist artist = findArtist(album);
//                    artist.setLikes(artist.getLikes()+1);
//                    return song;
//                }
//
//            }
//        }
//        return null;




        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        // check for song existance
        Song currSong = null;
        for(Song s: songs){
            if(songTitle.equals(s.getTitle())){
                currSong = s;
                break;
            }
        }
        if(currSong==null) throw new Exception("Song does not exist");

//        public HashMap<Song, List<User>> songLikeMap;
        List<User> likesList = songLikeMap.getOrDefault(currSong,new ArrayList<>());
        if(!likesList.contains(currUser)){
            likesList.add(currUser);
            songLikeMap.put(currSong,likesList);
            currSong.setLikes(currSong.getLikes()+1);


            // song -> album
            Album currAlbum = null;
            for(Album a: albumSongMap.keySet()){
                if(albumSongMap.get(a).contains(currSong)){
                    currAlbum = a;
                    break;
                }
            }
            // album -> artist
            Artist currArtist = null;
            for(Artist a: artistAlbumMap.keySet()){
                if(artistAlbumMap.get(a).contains(currAlbum)){
                    currArtist = a;
                    break;
                }
            }
            assert currArtist != null;
            currArtist.setLikes(currArtist.getLikes()+1);
        }
        return currSong;


    }
//  ////////////  finding album using song
//    public Album findAlbum(Song song) {
//        for(Album album:albumSongMap.keySet()) {
//            List<Song> list = albumSongMap.get(album);
//            if(list.contains(song)) return album;
//        }
//        return null;
//    }
//
// /////////////   // Finding Artist using album
//    public Artist findArtist(Album album) {
//        for(Artist artist:artistAlbumMap.keySet()) {
//            List<Album> list = artistAlbumMap.get(artist);
//            if(list.contains(album)) return artist;
//        }
//        return null;
//    }
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
