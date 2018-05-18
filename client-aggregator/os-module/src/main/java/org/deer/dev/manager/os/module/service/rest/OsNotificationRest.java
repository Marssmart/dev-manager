/*
 *
 * (C) Copyright 2018 Ján Srniček (https://github.com/Marssmart)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.deer.dev.manager.os.module.service.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.deer.dev.manager.common.ThrowingInvoker;
import org.deer.dev.manager.os.module.service.service.SystemNotificationService;
import org.deer.dev.manager.os.module.service.service.SystemNotificationService.NotificationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/os/notifications")
public class OsNotificationRest {

  private static final Logger LOG = LoggerFactory.getLogger(OsNotificationRest.class);

  @Autowired
  private SystemNotificationService notificationService;

  @RequestMapping(path = "/show-info", method = GET)
  public ResponseEntity<Void> showInfoNotification(@RequestParam("message") String message) {
    return invokeNotificationAndReportHttpStatus(
        () -> notificationService.showInfoMessage(message));
  }

  @RequestMapping(path = "/show-warning", method = GET)
  public ResponseEntity<Void> showWarningNotification(@RequestParam("message") String message) {
    return invokeNotificationAndReportHttpStatus(
        () -> notificationService.showWarningMessage(message));
  }

  @RequestMapping(path = "/show-error", method = GET)
  public ResponseEntity<Void> showErrorNotification(@RequestParam("message") String message) {
    return invokeNotificationAndReportHttpStatus(
        () -> notificationService.showErrorMessage(message));
  }

  private static ResponseEntity<Void> invokeNotificationAndReportHttpStatus(
      final ThrowingInvoker<NotificationFailedException> notificationThrowingInvoker) {
    try {
      notificationThrowingInvoker.invoke();
    } catch (NotificationFailedException e) {
      LOG.error("Error while raising notification", e);
      return ResponseEntity.status(500).build();
    }
    return ResponseEntity.ok().build();
  }
}
