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

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.deer.dev.manager.os.module.service.dto.PortRange;
import org.deer.dev.manager.os.module.service.dto.PortRangeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NetworkingServiceImpl implements NetworkingService {

  private static final Logger LOG = LoggerFactory.getLogger(NetworkingServiceImpl.class);

  @Override
  public CompletableFuture<Set<Integer>> scanAvailableTcpPorts(final PortRange portRange) {
    final int batchSize = 5;
    final int nrOfBatches = portRange.batches(batchSize);
    final int rangeStart = portRange.getRangeStart();
    final int rangeEnd = portRange.getRangeEnd();

    final CompletableFuture[] subRangeFutures = IntStream.range(0, nrOfBatches)
        .mapToObj(batchIndex -> {
          final int subRangeStart = rangeStart + (batchIndex * batchSize);
          final int subRangeEnd = Math.min(subRangeStart + batchSize, rangeEnd);
          return new PortRangeBuilder()
              .setRangeStart(subRangeStart)
              .setRangeEnd(subRangeEnd)
              .createPortRange();
        })
        .map(subRange -> CompletableFuture.supplyAsync(() -> portRangeScanTask(subRange)))
        .toArray(CompletableFuture[]::new);

    return CompletableFuture.allOf(subRangeFutures)
        .thenApply(aVoid -> Arrays.stream(subRangeFutures)
            .map(completableFuture -> (CompletableFuture<Set<Integer>>) completableFuture)
            .map(CompletableFuture::join)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()));
  }

  private static Set<Integer> portRangeScanTask(final PortRange subRange) {
    Set<Integer> availablePorts = new HashSet<>();
    final int rangeStart = subRange.getRangeStart();
    final int rangeEnd = subRange.getRangeEnd();
    LOG.debug("Processing range[{} - {}]", rangeStart, rangeEnd);
    for (int i = rangeStart; i < rangeEnd; i++) {
      try (final Socket socket = new Socket("localhost", i);) {
        LOG.debug("Port {} available", i);
        availablePorts.add(i);
      } catch (IOException e) {
        // unable to add open socket - port is not available
      }
    }
    return availablePorts;
  }
}