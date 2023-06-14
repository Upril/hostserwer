package com.serwertetowy.services.implementations;
import com.serwertetowy.entities.*;
import com.serwertetowy.exceptions.UserNotFoundException;
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
    RatingRepository ratingRepository;
    @Autowired
    EpisodesService episodesService;
    @Autowired
    WatchFlagRepository watchFlagRepository;
    @Override
    public User registerUser(User user) throws IOException {
        //in the future used for password encription
        user.setPassword(user.getPassword());
        //if no pic is sent, use a default one
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
    @Override
    public List<RatingSummary> getUserRatingsById(Long id) {
        return ratingRepository.findByUserId(id);
    }
    @Override
    public Resource getUserImage(Long id){
        byte[] image = userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getImageData();
        return new ByteArrayResource(image);
    }
    @Override
    public List<UserSeriesSummary> getWatchlist(Long id) {
        List<UserSeriesData> userSeriesList = userSeriesRepository.findByUserId(id);
        List<UserSeriesSummary> userSeriesSummaryList = new ArrayList<>();
        //convert userseriesdata, which contains only user and series id, into a proper watchlist dto
        for (UserSeriesData userSeries: userSeriesList){
            userSeriesSummaryList.add(new UserSeriesSummary() {
                @Override
                public Long getId() {
                    return userSeries.getId();
                }

                @Override
                public SeriesSummary getSeriesSummary() {
                    SeriesSummary summary = new SeriesSummary();
                    Series series = seriesRepository.findById(userSeries.getSeriesId().intValue()).orElseThrow(()->
                            new ResponseStatusException(HttpStatus.NOT_FOUND));
                    summary.setId(series.getId());
                    summary.setName(series.getName());
                    summary.setDescription(series.getDescription());
                    summary.setEpisodes(episodesService.getEpisodesBySeries(userSeries.getSeriesId().intValue()));
                    List<Tags> tags = new ArrayList<>();
                    for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(series.getId())){
                        Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue()).orElseThrow(()->
                                new ResponseStatusException(HttpStatus.NOT_FOUND));
                        tags.add(foundTags);
                    }
                    summary.setSeriesTags(tags);
                    return summary;
                }

                @Override
                public UserSummary getUserSummary() {
                    return null;
                }

                @Override
                public WatchFlags getWatchflag() {
                    return watchFlagRepository.findById(userSeries.getWatchFlagsId().intValue()).orElseThrow(()->
                            new ResponseStatusException(HttpStatus.NOT_FOUND));
                }
            });

        }
        return userSeriesSummaryList;
    }
    @Override
    public void putUserImage(MultipartFile file, Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setImageData(file.getBytes());
        userRepository.save(user);
    }
    @Override
    public UserSummary putUser(Long id, String firstname, String lastname, String email) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        userRepository.save(user);
        return userRepository.findByEmail(email);
    }

}
