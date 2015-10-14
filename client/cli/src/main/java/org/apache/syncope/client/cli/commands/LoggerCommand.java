/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.client.cli.commands;

import javax.xml.ws.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.syncope.client.cli.Command;
import org.apache.syncope.client.cli.Input;
import org.apache.syncope.client.cli.SyncopeServices;
import org.apache.syncope.client.cli.messages.UsageMessages;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.LoggerTO;
import org.apache.syncope.common.lib.types.LoggerLevel;
import org.apache.syncope.common.lib.types.LoggerType;
import org.apache.syncope.common.rest.api.service.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(name = "logger")
public class LoggerCommand extends AbstractCommand {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerCommand.class);

    private static final String HELP_MESSAGE = "Usage: logger [options]\n"
            + "  Options:\n"
            + "    --help \n"
            + "    --list \n"
            + "    --read \n"
            + "       Syntax: --read {LOG-NAME} {LOG-NAME} [...]\n"
            + "    --update \n"
            + "       Syntax: --update {LOG-NAME}={LOG-LEVEL} {LOG-NAME}={LOG-LEVEL} [...]\n"
            + "    --update-all \n"
            + "       Syntax: --update-all {LOG-LEVEL} \n"
            + "    --create \n"
            + "       Syntax: --create {LOG-NAME}={LOG-LEVEL} {LOG-NAME}={LOG-LEVEL} [...]\n"
            + "    --delete \n"
            + "       Syntax: --delete {LOG-NAME} {LOG-NAME} [...]";

    @Override
    public void execute(final Input input) {
        LOG.debug("Option: {}", input.getOption());
        LOG.debug("Parameters:");
        for (final String parameter : input.getParameters()) {
            LOG.debug("   > " + parameter);
        }

        final String[] parameters = input.getParameters();

        if (StringUtils.isBlank(input.getOption())) {
            input.setOption(Options.HELP.getOptionName());
        }

        final LoggerService loggerService = SyncopeServices.get(LoggerService.class);
        switch (Options.fromName(input.getOption())) {
            case LIST:
                try {
                    System.out.println("\n");
                    for (final LoggerTO loggerTO : loggerService.list(LoggerType.LOG)) {
                        System.out.println(" - " + loggerTO.getKey() + " -> " + loggerTO.getLevel());
                        System.out.println("");
                    }
                } catch (final SyncopeClientException ex) {
                    UsageMessages.printErrorMessage("Error: " + ex.getMessage());
                }
                break;
            case READ:
                final String readErrorMessage = UsageMessages.optionCommandMessage(
                        "logger --read {LOG-NAME} {LOG-NAME} [...]");
                if (parameters.length >= 1) {
                    for (final String parameter : parameters) {
                        try {
                            final LoggerTO loggerTO = loggerService.read(LoggerType.LOG, parameter);
                            System.out.println("\n - Logger");
                            System.out.println("   - key: " + loggerTO.getKey());
                            System.out.println("   - level: " + loggerTO.getLevel());
                            System.out.println("");
                        } catch (final SyncopeClientException | WebServiceException ex) {
                            if (ex.getMessage().startsWith("NotFound")) {
                                UsageMessages.printErrorMessage(
                                        "Logger " + parameter + " doesn't exist!");
                            } else {
                                UsageMessages.printErrorMessage("Error: " + ex.getMessage());
                            }
                        }
                    }
                } else {
                    System.out.println(readErrorMessage);
                }
                break;
            case UPDATE:
                final String updateErrorMessage = UsageMessages.optionCommandMessage(
                        "logger --update {LOG-NAME}={LOG-LEVEL} {LOG-NAME}={LOG-LEVEL} [...]");

                if (parameters.length >= 1) {
                    Input.PairParameter pairParameter = null;
                    for (final String parameter : parameters) {
                        try {
                            pairParameter = input.toPairParameter(parameter);
                            final LoggerTO loggerTO = loggerService.read(LoggerType.LOG, pairParameter.getKey());
                            loggerTO.setLevel(LoggerLevel.valueOf(pairParameter.getValue()));
                            loggerService.update(LoggerType.LOG, loggerTO);
                            System.out.
                                    println("\n - Logger " + loggerTO.getKey() + " updated");
                            System.out.println("   - new level: " + loggerTO.getLevel());
                            System.out.println("");
                        } catch (final WebServiceException | SyncopeClientException | IllegalArgumentException ex) {
                            if (ex.getMessage().startsWith("No enum constant org.apache.syncope.common.lib.types.")) {
                                UsageMessages.printErrorMessage(ex.getMessage());
                                System.out.println("Try with:");
                                for (final LoggerLevel level : LoggerLevel.values()) {
                                    System.out.println("  *** " + level.name());
                                }
                                System.out.println("");
                            } else if ("Parameter syntax error!".equalsIgnoreCase(ex.getMessage())) {
                                UsageMessages.printErrorMessage(ex.getMessage(), updateErrorMessage);
                            } else if (ex.getMessage().startsWith("NotFound")) {
                                UsageMessages.printErrorMessage(
                                        "Logger " + pairParameter.getKey() + " doesn't exists!");
                            } else {
                                UsageMessages.printErrorMessage(ex.getMessage(), updateErrorMessage);
                            }
                            break;
                        }
                    }
                } else {
                    System.out.println(updateErrorMessage);
                }
                break;
            case UPDATE_ALL:
                final String updateAllErrorMessage = UsageMessages.optionCommandMessage(
                        "logger --update-all {LOG-LEVEL}");

                if (parameters.length == 1) {
                    for (final LoggerTO loggerTO : loggerService.list(LoggerType.LOG)) {
                        try {
                            loggerTO.setLevel(LoggerLevel.valueOf(parameters[0]));
                            loggerService.update(LoggerType.LOG, loggerTO);
                            System.out.
                                    println("\n - Logger " + loggerTO.getKey() + " updated");
                            System.out.println("   - new level: " + loggerTO.getLevel());
                            System.out.println("");
                        } catch (final WebServiceException | SyncopeClientException | IllegalArgumentException ex) {
                            if (ex.getMessage().startsWith("No enum constant org.apache.syncope.common.lib.types.")) {
                                UsageMessages.printErrorMessage(ex.getMessage());
                                System.out.println("Try with:");
                                for (final LoggerLevel level : LoggerLevel.values()) {
                                    System.out.println("  *** " + level.name());
                                }
                                System.out.println("");
                            } else {
                                UsageMessages.printErrorMessage(ex.getMessage(), updateAllErrorMessage);
                            }
                            break;
                        }
                    }
                } else {
                    System.out.println(updateAllErrorMessage);
                }
                break;
            case CREATE:
                final String createErrorMessage = UsageMessages.optionCommandMessage(
                        "logger --create {LOG-NAME}={LOG-LEVEL} {LOG-NAME}={LOG-LEVEL} [...]");

                if (parameters.length >= 1) {
                    Input.PairParameter pairParameter;
                    LoggerTO loggerTO;
                    for (final String parameter : parameters) {
                        loggerTO = new LoggerTO();
                        try {
                            pairParameter = input.toPairParameter(parameter);
                            loggerTO.setKey(pairParameter.getKey());
                            loggerTO.setLevel(LoggerLevel.valueOf(pairParameter.getValue()));
                            loggerService.update(LoggerType.LOG, loggerTO);
                            System.out.
                                    println("\n - Logger " + loggerTO.getKey() + " updated");
                            System.out.println("   - level: " + loggerTO.getLevel());
                            System.out.println("");
                        } catch (final WebServiceException | SyncopeClientException | IllegalArgumentException ex) {
                            if (ex.getMessage().startsWith("No enum constant org.apache.syncope.common.lib.types.")) {
                                UsageMessages.printErrorMessage(ex.getMessage());
                                System.out.println("Try with:");
                                for (final LoggerLevel level : LoggerLevel.values()) {
                                    System.out.println("  *** " + level.name());
                                }
                                System.out.println("");
                            } else if ("Parameter syntax error!".equalsIgnoreCase(ex.getMessage())) {
                                UsageMessages.printErrorMessage(ex.getMessage(), createErrorMessage);
                            }
                            break;
                        }
                    }
                } else {
                    System.out.println(createErrorMessage);
                }
                break;
            case DELETE:
                final String deleteErrorMessage = UsageMessages.optionCommandMessage(
                        "logger --delete {LOG-NAME} {LOG-NAME} [...]");

                if (parameters.length >= 1) {
                    for (final String parameter : parameters) {
                        try {
                            loggerService.delete(LoggerType.LOG, parameter);
                            System.out.println("\n - Logger " + parameter + " deleted!\n");
                        } catch (final WebServiceException | SyncopeClientException ex) {
                            if (ex.getMessage().startsWith("NotFound")) {
                                UsageMessages.printErrorMessage(
                                        "Logger " + parameter + " doesn't exists!");
                            } else {
                                UsageMessages.printErrorMessage(ex.getMessage());
                            }
                        }
                    }
                } else {
                    System.out.println(deleteErrorMessage);
                }
                break;
            case HELP:
                System.out.println(HELP_MESSAGE);
                break;
            default:
                System.out.println(input.getOption() + " is not a valid option.");
                System.out.println("");
                System.out.println(HELP_MESSAGE);
        }
    }

    private enum Options {

        HELP("--help"),
        LIST("--list"),
        READ("--read"),
        UPDATE("--update"),
        UPDATE_ALL("--update-all"),
        CREATE("--create"),
        DELETE("--delete");

        private final String optionName;

        private Options(final String optionName) {
            this.optionName = optionName;
        }

        public String getOptionName() {
            return optionName;
        }

        public boolean equalsOptionName(final String otherName) {
            return (otherName == null) ? false : optionName.equals(otherName);
        }

        public static Options fromName(final String name) {
            Options optionToReturn = HELP;
            for (final Options option : Options.values()) {
                if (option.equalsOptionName(name)) {
                    optionToReturn = option;
                }
            }
            return optionToReturn;
        }
    }

}
