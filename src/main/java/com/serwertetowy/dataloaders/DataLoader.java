package com.serwertetowy.dataloaders;

import com.serwertetowy.entities.*;
import com.serwertetowy.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    SeriesRepository seriesRepository;
    @Autowired
    SeriesTagsRepository seriesTagsRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WatchFlagRepository watchFlagRepository;
    @Autowired
    UserSeriesRepository userSeriesRepository;
    @Override
    public void run(String... args) throws Exception {
        loadSeriesData();
        loadTagData();
        loadSeriesTagsData();
        loadUserData();
        loadWatchFlagsData();
        loadUserSeries();
        long series = seriesRepository.count();
        long tags = tagRepository.count();
        long stags = seriesTagsRepository.count();
        long users = userRepository.count();
        long watchflags = watchFlagRepository.count();
        long userSeries = userSeriesRepository.count();
        System.out.println("Series count: "+series);
        System.out.println("Tags count: "+tags);
        System.out.println("SeriesTags count: "+stags);
        System.out.println("Users count: "+users);
        System.out.println("Watchflags count: "+watchflags);
        System.out.println("UserSeries count: "+userSeries);
    }
    private void loadSeriesData(){
        if(seriesRepository.count() == 0){
            Series series1 = new Series(1L, "Haikyuu!!", "The series revolves around a fairly large ensemble consisting of a high school volleyball team.",null,null,null);
            Series series2 = new Series(2L, "Dragon Ball Z", "Revolves around superhuman martial artist Goku and his friends testing their mettle against an increasingly powerful and outlandish series of foes.",null,null,null);
            Series series3 = new Series(3L, "My Hero Academia", "focusing on Izuku Midoriya, a hero who was born without powers (but is determined to save people regardless) in a world full of folks with abilities. MHA constantly finds ways to challenge Midoriya's traditional hero's journey",null,null,null);
            Series series4 = new Series(4L, "Jojo's Bizarre Adventure", "Jojo's Bizarre Adventure: Epic generational battles of Joestar family against supernatural foes with unique powers. Eccentric characters, mind-bending plot. Legendary manga/anime.",null,null,null);
            Series series5 = new Series(5L, "Naruto Shippuden", "Continuation of Naruto's journey as he trains to become a powerful ninja, facing formidable enemies, uncovering secrets, and protecting his village. Action-packed, emotional, and explores themes of friendship and perseverance.",null,null,null);
            Series series6 = new Series(6L, "Death Note", "Genius student Light Yagami discovers a supernatural notebook that kills anyone whose name is written in it. He seeks to create a new world order, engaging in a gripping cat-and-mouse game with detective L. Intense mind games and a psychological thriller",null,null,null);
            Series series7 = new Series(7L, "Attack on Titan", "Humanity fights for survival against giant humanoid creatures called Titans. Eren Yeager and his friends join the military to uncover the mysteries of the Titans and protect what remains of humanity.",null,null,null);
            Series series8 = new Series(8L, "Demon Slayer", "Tanjiro Kamado becomes a demon slayer after his family is slaughtered and his sister transformed into a demon. With a blade forged from tragedy, he embarks on a quest to avenge his family and find a cure for his sister.",null,null,null);
            Series series9 = new Series(9L, "One Piece", "Monkey D. Luffy sets sail with his crew, the Straw Hat Pirates, in search of the ultimate treasure, the One Piece, while battling powerful foes, exploring diverse islands, and forming alliances.",null,null,null);
            Series series10 = new Series(10L, "Fullmetal Alchemist: Brotherhood", "Two brothers, Edward and Alphonse Elric, use alchemy in their quest to find the Philosopher's Stone and restore their bodies. They face political intrigue, moral dilemmas, and encounters with powerful alchemists.",null,null,null);

            seriesRepository.save(series1);
            seriesRepository.save(series2);
            seriesRepository.save(series3);
            seriesRepository.save(series4);
            seriesRepository.save(series5);
            seriesRepository.save(series6);
            seriesRepository.save(series7);
            seriesRepository.save(series8);
            seriesRepository.save(series9);
            seriesRepository.save(series10);
        }
    }
    private void loadTagData(){
        if(tagRepository.count() == 0){
            Tags tag1 = new Tags(1L,"Sports");
            Tags tag2 = new Tags(2L,"Character Development");
            Tags tag3 = new Tags(3L,"Superheroes");
            Tags tag4 = new Tags(4L,"Humor");
            Tags tag5 = new Tags(5L,"Afterlife");
            Tags tag6 = new Tags(6L,"Shounen");
            Tags tag7 = new Tags(7L,"Action");
            Tags tag8 = new Tags(8L,"Bizzare");
            Tags tag9 = new Tags(9L,"Thriller");
            Tags tag10 = new Tags(10L,"Psychological");
            Tags tag11 = new Tags(11L,"Beautiful animation");
            Tags tag12 = new Tags(12L,"Redemption");
            Tags tag13 = new Tags(13L,"Adventure");
            Tags tag14 = new Tags(14L,"Angels");
            Tags tag15 = new Tags(15L,"Apocalypse");
            Tags tag16 = new Tags(16L,"Art");
            Tags tag17 = new Tags(17L,"Astronomy");
            Tags tag18 = new Tags(18L,"Aviation");
            Tags tag19 = new Tags(19L,"Baking");
            Tags tag20 = new Tags(20L,"Hentai");

            tagRepository.save(tag1);
            tagRepository.save(tag2);
            tagRepository.save(tag3);
            tagRepository.save(tag4);
            tagRepository.save(tag5);
            tagRepository.save(tag6);
            tagRepository.save(tag7);
            tagRepository.save(tag8);
            tagRepository.save(tag9);
            tagRepository.save(tag10);
            tagRepository.save(tag11);
            tagRepository.save(tag12);
            tagRepository.save(tag13);
            tagRepository.save(tag14);
            tagRepository.save(tag15);
            tagRepository.save(tag16);
            tagRepository.save(tag17);
            tagRepository.save(tag18);
            tagRepository.save(tag19);
            tagRepository.save(tag20);
        }
    }
    private void loadSeriesTagsData(){
        if (seriesTagsRepository.count() == 0) {
            Series series1 = null;
            Series series2= null;
            Series series3= null;
            Series series4= null;
            Series series5= null;
            Series series6= null;
            Series series7= null;
            Series series8= null;
            Series series9= null;
            Series series10= null;

            Tags tag1 = null;
            Tags tag2 = null;
            Tags tag3 = null;
            Tags tag4 = null;
            Tags tag6 = null;
            Tags tag7 = null;
            Tags tag8 = null;
            Tags tag9 = null;
            Tags tag10 = null;
            Tags tag11 = null;
            Tags tag12 = null;
            Tags tag13 = null;

            Optional<Series> seriesO1 = seriesRepository.findById(1);
            if(seriesO1.isPresent()) series1 = seriesO1.get();
            Optional<Series> seriesO2 = seriesRepository.findById(2);
            if(seriesO2.isPresent()) series2 = seriesO2.get();
            Optional<Series> seriesO3 = seriesRepository.findById(3);
            if(seriesO3.isPresent()) series3 = seriesO3.get();
            Optional<Series> seriesO4 = seriesRepository.findById(4);
            if(seriesO4.isPresent()) series4 = seriesO4.get();
            Optional<Series> seriesO5 = seriesRepository.findById(5);
            if(seriesO5.isPresent()) series5 = seriesO5.get();
            Optional<Series> seriesO6 = seriesRepository.findById(6);
            if(seriesO6.isPresent()) series6 = seriesO6.get();
            Optional<Series> seriesO7 = seriesRepository.findById(7);
            if(seriesO7.isPresent()) series7 = seriesO7.get();
            Optional<Series> seriesO8 = seriesRepository.findById(8);
            if(seriesO8.isPresent()) series8 = seriesO8.get();
            Optional<Series> seriesO9 = seriesRepository.findById(9);
            if(seriesO9.isPresent()) series9 = seriesO9.get();
            Optional<Series> seriesO10 =seriesRepository.findById(10);
            if(seriesO10.isPresent()) series10 = seriesO10.get();

            Optional<Tags> tagO1 = tagRepository.findById(1);
            if(tagO1.isPresent()) tag1 = tagO1.get();
            Optional<Tags> tagO2 = tagRepository.findById(1);
            if(tagO2.isPresent()) tag2 = tagO2.get();
            Optional<Tags> tagO3 = tagRepository.findById(1);
            if(tagO3.isPresent()) tag3 = tagO3.get();
            Optional<Tags> tagO4 = tagRepository.findById(1);
            if(tagO4.isPresent()) tag4 = tagO4.get();
            Optional<Tags> tagO6 = tagRepository.findById(1);
            if(tagO6.isPresent()) tag6 = tagO6.get();
            Optional<Tags> tagO7 = tagRepository.findById(1);
            if(tagO7.isPresent()) tag7 = tagO7.get();
            Optional<Tags> tagO8 = tagRepository.findById(1);
            if(tagO8.isPresent()) tag8 = tagO8.get();
            Optional<Tags> tagO9 = tagRepository.findById(1);
            if(tagO9.isPresent()) tag9 = tagO9.get();
            Optional<Tags> tagO10 = tagRepository.findById(1);
            if(tagO10.isPresent()) tag10 = tagO10.get();
            Optional<Tags> tagO11 = tagRepository.findById(1);
            if(tagO11.isPresent()) tag11 = tagO11.get();
            Optional<Tags> tagO12 = tagRepository.findById(1);
            if(tagO12.isPresent()) tag12 = tagO12.get();
            Optional<Tags> tagO13 = tagRepository.findById(1);
            if(tagO13.isPresent()) tag13 = tagO13.get();



            SeriesTags tags1 = new SeriesTags(1L,series1,tag1);
            SeriesTags tags2 = new SeriesTags(2L,series1, tag2);
            SeriesTags tags3 = new SeriesTags(3L,series2,tag6);
            SeriesTags tags4 = new SeriesTags(4L,series2,tag7);
            SeriesTags tags5 = new SeriesTags(5L,series2,tag4);
            SeriesTags tags6 = new SeriesTags(6L,series3,tag7);
            SeriesTags tags7 = new SeriesTags(7L,series3, tag3);
            SeriesTags tags8 = new SeriesTags(8L,series3,tag6);
            SeriesTags tags9 = new SeriesTags(9L,series4,tag2);
            SeriesTags tags10 = new SeriesTags(10L,series4,tag4);
            SeriesTags tags11 = new SeriesTags(11L,series4, tag8);
            SeriesTags tags12 = new SeriesTags(12L, series5,tag6);
            SeriesTags tags13 = new SeriesTags(13L,series6,tag10);
            SeriesTags tags14 = new SeriesTags(14L,series6, tag9);
            SeriesTags tags15 = new SeriesTags(15L,series7,tag7);
            SeriesTags tags16 = new SeriesTags(16L,series8,tag11);
            SeriesTags tags17 = new SeriesTags(17L,series8,tag7);
            SeriesTags tags18 = new SeriesTags(18L,series9,tag6);
            SeriesTags tags19 = new SeriesTags(19L,series9,tag13);
            SeriesTags tags20 = new SeriesTags(20L,series10,tag12);
            SeriesTags tags21 = new SeriesTags(21L,series10,tag2);

            seriesTagsRepository.save(tags1);
            seriesTagsRepository.save(tags2);
            seriesTagsRepository.save(tags3);
            seriesTagsRepository.save(tags4);
            seriesTagsRepository.save(tags5);
            seriesTagsRepository.save(tags6);
            seriesTagsRepository.save(tags7);
            seriesTagsRepository.save(tags8);
            seriesTagsRepository.save(tags9);
            seriesTagsRepository.save(tags10);
            seriesTagsRepository.save(tags11);
            seriesTagsRepository.save(tags12);
            seriesTagsRepository.save(tags13);
            seriesTagsRepository.save(tags14);
            seriesTagsRepository.save(tags15);
            seriesTagsRepository.save(tags16);
            seriesTagsRepository.save(tags17);
            seriesTagsRepository.save(tags18);
            seriesTagsRepository.save(tags19);
            seriesTagsRepository.save(tags20);
            seriesTagsRepository.save(tags21);
        }
    }
    private void loadUserData(){
        if(userRepository.count() == 0) {
            User user1 = new User();
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setEmail("john@doe.com");
            user1.setPassword("123456");

            User user2 = new User();
            user2.setFirstName("Baba");
            user2.setLastName("Booey");
            user2.setEmail("baba@booey.com");
            user2.setPassword("123456");

            User user3 = new User();
            user3.setFirstName("John");
            user3.setLastName("Darksouls");
            user3.setEmail("darksoulsiseasy@gg.com");
            user3.setPassword("123456");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        }
    }
    private void loadWatchFlagsData(){
        if(watchFlagRepository.count() == 0) {
            WatchFlags watchFlag1 = new WatchFlags(1L, "Watching");
            WatchFlags watchFlag2 = new WatchFlags(2L, "Finished");
            WatchFlags watchFlag3 = new WatchFlags(3L, "Not Watched");
            WatchFlags watchFlag4 = new WatchFlags(4L, "Ignored");

            watchFlagRepository.save(watchFlag1);
            watchFlagRepository.save(watchFlag2);
            watchFlagRepository.save(watchFlag3);
            watchFlagRepository.save(watchFlag4);
        }
    }
    private void loadUserSeries(){
        if(userSeriesRepository.count() == 0){
            User u1 = null;
            User u2 = null;
            User u3 = null;
            Optional<User> userO1 = userRepository.findById(1L);
            if(userO1.isPresent()) u1 = userO1.get();
            Optional<User> userO2 = userRepository.findById(2L);
            if(userO2.isPresent()) u2 = userO2.get();
            Optional<User> userO3 = userRepository.findById(3L);
            if(userO3.isPresent()) u3 = userO3.get();

            Series series1 = null;
            Series series4= null;
            Series series6= null;
            Series series7= null;
            Series series8= null;
            Series series9= null;

            Optional<Series> seriesO1 = seriesRepository.findById(1);
            if(seriesO1.isPresent()) series1 = seriesO1.get();
            Optional<Series> seriesO4 = seriesRepository.findById(4);
            if(seriesO4.isPresent()) series4 = seriesO4.get();
            Optional<Series> seriesO6 = seriesRepository.findById(6);
            if(seriesO6.isPresent()) series6 = seriesO6.get();
            Optional<Series> seriesO7 = seriesRepository.findById(7);
            if(seriesO7.isPresent()) series7 = seriesO7.get();
            Optional<Series> seriesO8 = seriesRepository.findById(8);
            if(seriesO8.isPresent()) series8 = seriesO8.get();
            Optional<Series> seriesO9 = seriesRepository.findById(9);
            if(seriesO9.isPresent()) series9 = seriesO9.get();

            WatchFlags watchFlags1 = null;
            WatchFlags watchFlags2 = null;
            WatchFlags watchFlags3 = null;
            WatchFlags watchFlags4 = null;

            Optional<WatchFlags> watchFlagsO1 = watchFlagRepository.findById(1);
            if(watchFlagsO1.isPresent()) watchFlags1 = watchFlagsO1.get();
            Optional<WatchFlags> watchFlagsO2 = watchFlagRepository.findById(2);
            if(watchFlagsO2.isPresent()) watchFlags2 = watchFlagsO2.get();
            Optional<WatchFlags> watchFlagsO3 = watchFlagRepository.findById(3);
            if(watchFlagsO3.isPresent()) watchFlags3 = watchFlagsO3.get();
            Optional<WatchFlags> watchFlagsO4 = watchFlagRepository.findById(4);
            if(watchFlagsO4.isPresent()) watchFlags4 = watchFlagsO4.get();

            UserSeries us1 = new UserSeries(u1,series4,watchFlags1);
            UserSeries us2 = new UserSeries(u1,series1,watchFlags3);
            UserSeries us3 = new UserSeries(u2,series8,watchFlags2);
            UserSeries us4 = new UserSeries(u2,series4,watchFlags1);
            UserSeries us5 = new UserSeries(u3,series4,watchFlags4);
            UserSeries us6 = new UserSeries(u3,series7,watchFlags1);
            UserSeries us7 = new UserSeries(u3,series6,watchFlags1);
            UserSeries us8 = new UserSeries(u1,series9,watchFlags2);

            List<UserSeries> userSeriesList = List.of(us1,us2,us3,us4,us5,us6,us7,us8);
            userSeriesRepository.saveAll(userSeriesList);

        }
    }
}
