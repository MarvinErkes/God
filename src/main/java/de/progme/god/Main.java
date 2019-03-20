/*
 * Copyright (c) 2016 "Marvin Erkes"
 *
 * This file is part of God.
 *
 * God is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.progme.god;

import de.progme.god.util.Mode;
import de.progme.god.util.PipelineUtils;
import de.progme.iris.Iris;
import de.progme.iris.IrisConfig;
import de.progme.iris.config.Header;
import de.progme.iris.config.Key;
import de.progme.iris.config.Value;
import de.progme.iris.exception.IrisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Marvin Erkes on 26.06.2016.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Starting God");

        File config = new File("config.iris");
        if (!config.exists()) {
            try {
                Files.copy(Main.class.getClassLoader().getResourceAsStream("config.iris"), config.toPath());
            } catch (IOException e) {
                logger.error("Unable to copy default config! No write permissions?", e);
                return;
            }
        }

        try {
            IrisConfig irisConfig = Iris.from(config)
                    .def(new Header("general"), new Key("mode"), new Value("tcp"))
                    .def(new Header("general"), new Key("server"), new Value("0.0.0.0"), new Value("80"))
                    .def(new Header("general"), new Key("backlog"), new Value("100"))
                    .def(new Header("general"), new Key("boss"), new Value(String.valueOf(PipelineUtils.DEFAULT_BOSS_THREADS)))
                    .def(new Header("general"), new Key("worker"), new Value(String.valueOf(PipelineUtils.DEFAULT_WORKER_THREADS)))
                    .def(new Header("general"), new Key("balance"), new Value("RANDOM"))
                    .def(new Header("general"), new Key("timeout"), new Value("60"), new Value("60"))
                    .def(new Header("general"), new Key("probe"), new Value("10000"))
                    .def(new Header("general"), new Key("debug"), new Value("true"))
                    .def(new Header("general"), new Key("stats"), new Value("true"))
                    .build();

            logger.info("Config loaded");

            String modeString = irisConfig.getHeader("general").getKey("mode").next().asString();
            Mode mode = Mode.valueOf(modeString.toUpperCase());
            if (mode == null) {
                logger.error("Invalid mode '{}'", modeString);
                return;
            }

            logger.info("Using mode: " + mode);

            God god = GodFactory.create(mode, irisConfig);
            god.start(mode);
            god.console();
        } catch (IrisException e) {
            logger.error("Unable to load config", e);
        }
    }
}
