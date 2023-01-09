package sf.ssf.sfort.steedstopper;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.SpawnReason;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tf.ssf.sfort.ini.SFIni;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Config {
	public static Set<SpawnReason> naturalOnly = null;
	public static Logger LOGGER = LogManager.getLogger();
	static {
		SFIni defIni = new SFIni();
		defIni.load(String.join("\n", new String[]{
				"; Allow only horses that spawn with the specified spawn reasons",
				"; available spawn reasons: " + Arrays.stream(SpawnReason.values()).map(Enum::name).collect(Collectors.joining(" | ")),
				"; example of multiple spawn reasons:",
				"; allowedSpawnReasons=natural",
				"; .=dispenser",
				"allowedSpawnReasons="
		}));
		File confFile = new File(
				FabricLoader.getInstance().getConfigDir().toString(),
				"SteedStopper.sf.ini"
		);
		if (!confFile.exists()) {
			try {
				Files.write(confFile.toPath(), defIni.toString().getBytes());
				LOGGER.log(Level.INFO,"tf.ssf.sfort.steedstopper successfully created config file");
				loadIni(defIni);
			} catch (IOException e) {
				LOGGER.log(Level.ERROR,"tf.ssf.sfort.steedstopper failed to create config file, using defaults", e);
			}
		} else {
			try {
				SFIni ini = new SFIni();
				String text = Files.readString(confFile.toPath());
				int hash = text.hashCode();
				ini.load(text);
				for (Map.Entry<String, List<SFIni.Data>> entry : defIni.data.entrySet()) {
					List<SFIni.Data> list = ini.data.get(entry.getKey());
					if (list == null || list.isEmpty()) {
						ini.data.put(entry.getKey(), entry.getValue());
					} else {
						list.get(0).comments = entry.getValue().get(0).comments;
					}
				}
				loadIni(ini);
				String iniStr = ini.toString();
				if (hash != iniStr.hashCode()) {
					Files.write(confFile.toPath(), iniStr.getBytes());
				}
			} catch (IOException e) {
				LOGGER.log(Level.ERROR, "tf.ssf.sfort.steedstopper failed to load config file, using defaults", e);
			}
		}
	}

	public static void loadIni(SFIni ini) {
		List<SFIni.Data> list = ini.data.get("allowedSpawnReasons");
		Set<SpawnReason> ret = new HashSet<>();
		for (int i=0; i < list.size();i++) {
			try {
				String val = list.get(i).val;
				if (val == null || val.isBlank()) continue;
				if (!ret.add(getSpawnReason(val))) {
					list.remove(i--);
				}
			} catch (IllegalArgumentException e) {
				LOGGER.log(Level.WARN, "tf.ssf.sfort.steedstopper failed to parse "+list.get(i).val+"as a spawnreason", e);
			}
		}
		if (!ret.isEmpty()) {
			naturalOnly = ret;
		}
	}
	public static SpawnReason getSpawnReason(String val) throws IllegalArgumentException {
		if (val == null) {
			throw new IllegalArgumentException("Failed to parse as spawnreason (key: " + val + ")");
		} else {
			try {
				return Enum.valueOf(SpawnReason.class, val.toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException var10) {
				StringJoiner joiner = new StringJoiner(", ");
				for(SpawnReason en : SpawnReason.values()) {
					joiner.add(en.name());
				}
				String joined = joiner.toString();
				throw new IllegalArgumentException("Failed to parse as spawnreason (key: " + val + ") valid values: " + joined + ", " + joined.toLowerCase(Locale.ROOT), var10);
			}
		}
	}
}
