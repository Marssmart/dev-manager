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

package org.deer.dev.manager.os.module.service.service;

import static java.awt.TrayIcon.MessageType.ERROR;
import static java.awt.TrayIcon.MessageType.INFO;
import static java.awt.TrayIcon.MessageType.WARNING;

import java.awt.AWTException;
import java.awt.TrayIcon.MessageType;
import javax.annotation.PostConstruct;
import org.deer.dev.manager.common.ThrowingInvoker;
import org.springframework.stereotype.Service;

@Service
public class SystemNotificationServiceImpl implements SystemNotificationService {

  private static final String INFO_ICON = "icons/notifications/info.png";
  private static final String WARNING_ICON = "icons/notifications/error.png";
  private static final String ERROR_ICON = "icons/notifications/error.png";
  private static final String DEV_MANAGER = "Dev-Manager";

  @PostConstruct
  public void init() {
    // SystemTray can't be retrieved in headless mode
    System.setProperty("java.awt.headless", "false");
  }

  @Override
  public void showInfoMessage(String message) throws NotificationFailedException {
    reportOnFailure(() -> showNotification(message, INFO, INFO_ICON));
  }

  @Override
  public void showWarningMessage(String message) throws NotificationFailedException {
    reportOnFailure(() -> showNotification(message, WARNING, WARNING_ICON));
  }

  @Override
  public void showErrorMessage(String message) throws NotificationFailedException {
    reportOnFailure(() -> showNotification(message, ERROR, ERROR_ICON));
  }

  private static void showNotification(final String message, final MessageType messageType,
      final String iconResource) throws AWTException {
    new SystemIconNotificationBuilder()
        .setMessageCaption(DEV_MANAGER)
        .setMessage(message)
        .setMessageType(messageType)
        .setIconResource(iconResource)
        .showNotification();
  }

  private void reportOnFailure(final ThrowingInvoker<AWTException> invoker)
      throws NotificationFailedException {
    try {
      invoker.invoke();
    } catch (AWTException e) {
      throw new NotificationFailedException("Unable to show notification", e);
    }

  }
}
