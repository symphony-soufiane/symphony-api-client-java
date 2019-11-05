package com.symphony.ms.songwriter.notification;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.symphony.ms.songwriter.internal.lib.jsonmapper.JsonMapper;
import com.symphony.ms.songwriter.internal.message.model.SymphonyMessage;
import com.symphony.ms.songwriter.internal.notification.NotificationInterceptor;
import com.symphony.ms.songwriter.internal.notification.model.NotificationRequest;

public class MyNotificationInterceptor extends NotificationInterceptor {
  private static final Logger LOGGER = LoggerFactory.getLogger(MyNotificationInterceptor.class);

  private JsonMapper jsonMapper;

  public MyNotificationInterceptor(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public boolean process(NotificationRequest notificationRequest,
      SymphonyMessage notificationMessage) {

    // For simplicity of this sample code identifier == streamId
    String streamId = notificationRequest.getIdentifier();

    if (streamId != null) {
      notificationRequest.setStreamId(streamId);
      notificationMessage.setMessage(notificationRequest.getPayload());
      Map<String, String> data = jsonMapper.toObject(notificationRequest.getPayload(), Map.class);
      notificationMessage.setEnrichedMessage(
          "<b>Notification received:</b><br />" + notificationRequest.getPayload(), // Default message when extension app not present
          "com.symphony.ms.notification", // Root node in the payload received in extension app
          data, // payload received in extension app
          "1.0"); // version
      return true; // true if notification interception chain should continue
    }

    return false; // false if notification interception chain should be stopped and notification request rejected
  }

}
