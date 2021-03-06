/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.whirr.cli.command;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import joptsimple.OptionSet;

import org.apache.whirr.service.ClusterSpec;
import org.apache.whirr.service.ServiceFactory;
import org.junit.Test;

public class AbstractClusterSpecCommandTest {

  @Test
  public void testOverrides() throws Exception {
    AbstractClusterSpecCommand clusterSpecCommand = new AbstractClusterSpecCommand("name",
        "description", new ServiceFactory()) {
      @Override
      public int run(InputStream in, PrintStream out, PrintStream err,
          List<String> args) throws Exception {
        return 0;
      }
    };

    OptionSet optionSet = clusterSpecCommand.parser.parse(
        "--service-name", "overridden-test-service",
        "--config", "whirr-override-test.properties");
    ClusterSpec clusterSpec = clusterSpecCommand.getClusterSpec(optionSet);
    assertThat(clusterSpec.getServiceName(), is("overridden-test-service"));
    assertThat(clusterSpec.getClusterName(), is("test-cluster"));
  }

  /**
   * Ensure that an invalid service name causes failure
   */
  @Test(expected=IllegalArgumentException.class)
  public void testCreateServerWithInvalidServiceName() throws Exception {
    AbstractClusterSpecCommand clusterSpecCommand = new AbstractClusterSpecCommand("name",
        "description", new ServiceFactory()) {
      @Override
      public int run(InputStream in, PrintStream out, PrintStream err,
          List<String> args) throws Exception {
        return 0;
      }
    };

    OptionSet optionSet = clusterSpecCommand.parser.parse(
        "--service-name", "foo",
        "--config", "whirr-override-test.properties");
    ClusterSpec clusterSpec = clusterSpecCommand.getClusterSpec(optionSet);
    // this should fail - non-existent service
    clusterSpecCommand.createService("bar");
  }
}
