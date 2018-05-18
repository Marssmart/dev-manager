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

import static org.junit.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon.MessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class SystemIconNotificationBuilderTest {

  private static final String INFO_ICON = "icons/notifications/info.png";
  private SystemIconNotificationBuilder notificationBuilder;

  @ClassRule
  public static final NonHeadlessEnvironmentRule TEST_RUN_RULE = new NonHeadlessEnvironmentRule();

  @Before
  public void init() {
    notificationBuilder = new SystemIconNotificationBuilder();
  }

  @After
  public void tearDown() throws Exception {
    notificationBuilder.close();
  }

  @Test(expected = NullPointerException.class)
  public void testInvalidNoIcon() throws AWTException {
    notificationBuilder.showNotification();
  }

  @Test(expected = NullPointerException.class)
  public void testInvalidNoMessageType() throws AWTException {
    notificationBuilder
        .setMessageType(MessageType.INFO)
        .showNotification();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNoMessageOrMessageCaption() throws AWTException {
    notificationBuilder

        .setMessageType(MessageType.INFO)
        .setIconResource(INFO_ICON)
        .showNotification();
  }

  @Test
  public void testValidInfoNotification() throws AWTException {
    final SystemIconNotificationBuilder presetBuilder = notificationBuilder
        .setIconResource(INFO_ICON)
        .setMessage("Test info message")
        .setMessageCaption("Test info caption")
        .setMessageType(MessageType.INFO);
    verifyNotificationShown(presetBuilder);
  }

  @Test
  public void testValidWarningNotification() throws AWTException {
    final SystemIconNotificationBuilder presetBuilder = notificationBuilder
        .setIconResource("icons/notifications/warning.png")
        .setMessage("Test warning message")
        .setMessageCaption("Test warning caption")
        .setMessageType(MessageType.WARNING);
    verifyNotificationShown(presetBuilder);
  }


  @Test
  public void testValidErrorNotification() throws AWTException {
    final SystemIconNotificationBuilder presetBuilder = notificationBuilder
        .setIconResource("icons/notifications/error.png")
        .setMessage("Test error message")
        .setMessageCaption("Test error caption")
        .setMessageType(MessageType.ERROR);
    verifyNotificationShown(presetBuilder);
  }

  /**
   * Not completely safe way to check, but SystemTray has static block so mocking won't work
   */
  private static void verifyNotificationShown(SystemIconNotificationBuilder presetBuilder)
      throws AWTException {
    final int trayIconCountBefore = SystemTray.getSystemTray().getTrayIcons().length;
    presetBuilder.showNotification();
    final int trayIconCountAfter = SystemTray.getSystemTray().getTrayIcons().length;
    assertEquals("Notification has not been shown", trayIconCountBefore, trayIconCountAfter - 1);
  }
}