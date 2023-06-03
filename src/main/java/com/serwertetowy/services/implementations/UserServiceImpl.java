package com.serwertetowy.services.implementations;
import com.serwertetowy.entities.SeriesTags;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.TagNotFoundException;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSeriesRepository userSeriesRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    SeriesRepository seriesRepository;
    @Autowired
    SeriesTagsRepository seriesTagsRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    EpisodesService episodesService;

    @Override
    public User registerUser(User user) throws IOException {
        user.setPassword(user.getPassword());
        user.setImageData(resourceLoader.getResource("classpath:/images/defalt.jpg").getContentAsByteArray());
        userRepository.save(user);
        return user;
    }
    @Override
    public User registerUserWithImage(User user, MultipartFile file) throws IOException {
        user.setPassword(user.getPassword());
        user.setImageData(file.getBytes());
        userRepository.save(user);
        return user;
    }
    @Override
    public List<UserSummary> getAllUsers() {
        return userRepository.findAllUserData();
    }
    @Override
    public UserSummary getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Resource getUserImage(Long id){
        byte[] image = userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)).getImageData();
        return new ByteArrayResource(image);
    }
    @Override
    public List<SeriesSummary> getWatchlist(Long id) {
        List<UserSeriesData> userSeriesList = userSeriesRepository.findByUserId(id);
        List<SeriesSummary> seriesSummaries = new ArrayList<>();
        for (UserSeriesData userSeries: userSeriesList){
            seriesSummaries.add(getSeriesById(userSeries.getSeriesId().intValue()));
        }
        return seriesSummaries;
    }
    private SeriesSummary getSeriesById(Integer id) {
        SeriesSummary summary = new SeriesSummary();
        SeriesData data = seriesRepository.findSeriesDataById(id);
        summary.setId(data.getId());
        summary.setName(data.getName());
        summary.setDescription(data.getDescription());
        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(summary.getId())){
            Optional<Tags> optionalTags = tagRepository.findById(sTag.getTags().getId().intValue());
            if (!optionalTags.isPresent()) throw new TagNotFoundException();
            else tags.add(optionalTags.get());
        }
        summary.setSeriesTags(tags);
        summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
        return summary;
    }


}
