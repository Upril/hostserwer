package com.serwertetowy.dataloaders;

import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TagDataLoader implements CommandLineRunner {
    @Autowired
    TagRepository tagRepository;
    @Override
    public void run(String... args) throws Exception {
        loadTagData();
    }
    private void loadTagData(){
        if(tagRepository.count() == 0){
            Tags tag1 = new Tags(1L,"Historical");
            Tags tag2 = new Tags(2L,"Abstract");
            Tags tag3 = new Tags(3L,"Acting");
            Tags tag4 = new Tags(4L,"Adventure");
            Tags tag5 = new Tags(5L,"Afterlife");
            Tags tag6 = new Tags(6L,"Agriculture");
            Tags tag7 = new Tags(7L,"Action");
            Tags tag8 = new Tags(8L,"Adult Couples");
            Tags tag9 = new Tags(9L,"Adult Industry");
            Tags tag10 = new Tags(10L,"Age Transformation");
            Tags tag11 = new Tags(11L,"Aliens");
            Tags tag12 = new Tags(12L,"Amnesia");
            Tags tag13 = new Tags(13L,"Androids");
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
        System.out.println(tagRepository.count());
    }
}
