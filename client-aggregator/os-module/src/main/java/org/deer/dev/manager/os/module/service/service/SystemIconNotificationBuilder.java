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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.Resources;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Builder simplifying the process of creating system notification
 */
public class SystemIconNotificationBuilder implements AutoCloseable {

  private URL iconResource;
  private String messageCaption;
  private MessageType messageType;
  private String iconTooltip;
  private String message;

  private final List<TrayIcon> shownNotifications = new LinkedList<>();

  public SystemIconNotificationBuilder setIconResource(String iconResourcePath) {
    this.iconResource = Resources.getResource(iconResourcePath);
    return this;
  }

  public SystemIconNotificationBuilder setMessageCaption(String messageCaption) {
    this.messageCaption = messageCaption;
    return this;
  }

  public SystemIconNotificationBuilder setMessageType(MessageType messageType) {
    this.messageType = messageType;
    return this;
  }

  public SystemIconNotificationBuilder setIconTooltip(String iconTooltip) {
    this.iconTooltip = iconTooltip;
    return this;
  }

  public SystemIconNotificationBuilder setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Invokes system notification
   */
  public void showNotification() throws AWTException {
    checkNotNull(iconResource, "Icon resource is null");
    checkNotNull(messageType, "Message type has to be specified");
    checkArgument(messageCaption != null || message != null,
        "One of [messageCaption,message] has to be specified");
    SystemTray tray = SystemTray.getSystemTray();

    if (iconResource != null) {
      ImageIcon icon = new ImageIcon(iconResource);
      Image image = icon.getImage();
      TrayIcon trayIcon = new TrayIcon(image);
      trayIcon.setImageAutoSize(true);

      if (iconTooltip != null) {
        trayIcon.setToolTip(iconTooltip);
      }
      tray.add(trayIcon);
      trayIcon.displayMessage(messageCaption, message, messageType);

      //to allow cleanup
      shownNotifications.add(trayIcon);
    }
  }

  @Override
  public void close() throws Exception {
    final SystemTray systemTray = SystemTray.getSystemTray();
    shownNotifications.forEach(systemTray::remove);
  }
}
