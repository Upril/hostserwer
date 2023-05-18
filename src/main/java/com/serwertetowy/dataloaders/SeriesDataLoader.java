package com.serwertetowy.dataloaders;

import com.serwertetowy.entities.Series;
import com.serwertetowy.repos.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class SeriesDataLoader implements CommandLineRunner {
    @Autowired
    SeriesRepository seriesRepository;
    @Override
    public void run(String... args) throws Exception {
        loadSeriesData();
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
        System.out.println("Series count: "+seriesRepository.count());
    }
}
