package com.example.authservice.config.redis;

import com.example.authservice.entities.UserActivity;
import com.example.authservice.entities.enums.UserActivityType;
import com.example.authservice.repo.UserActivityRepository;
import com.example.authservice.utils.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * @author nguyen
 * @created_date 18/11/2021
 */
public class MessagePublisherImpl implements MessagePublisher {

  private static final Logger logger = LoggerFactory.getLogger(MessagePublisherImpl.class);

  @Autowired
  UserActivityRepository userActivityRepository;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  @Autowired
  private ChannelTopic topic;


  public void publish(String message) {
    stringRedisTemplate.convertAndSend(topic.getTopic(), message);
    logger.info("data={}", message);
    JsonObject jsonObject = JsonUtils.toJsonObject(message);
    if (jsonObject.has("type")) {
      String type = jsonObject.get("type").getAsString();
      if (type.equals("expired")) {
        String key = jsonObject.get("key").getAsString();
        long timestamp = jsonObject.get("timestamp").getAsLong();

        List<String> keyElement = Arrays.asList(key.split(":"));
        String token = keyElement.get(keyElement.size() - 1);

        /**
         * save to base_user_activity LOGOUT event with timestamp
         */
        Date time_log = new Date(timestamp);
        List<UserActivity> userActivities = userActivityRepository.findByToken(token);
        UserActivity userActivity = userActivities.get(userActivities.size()-1);
        if (userActivity.getActivity().equals(UserActivityType.LOGIN.getValue())) {
            UserActivity activity = new UserActivity();
            activity.setActivity(UserActivityType.LOGOUT.getValue());
            activity.setCreateTime(time_log);
            activity.setToken(userActivity.getToken());
            activity.setUserId(userActivity.getUserId());
            activity.setIPAddress(null);
            userActivityRepository.save(activity);
          }

      }
    }
  }
}
