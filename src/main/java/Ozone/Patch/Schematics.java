/*
 * Copyright 2021 Itzbenz
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
 */

package Ozone.Patch;

import Atom.Utility.Encoder;
import Atom.Utility.Pool;
import Ozone.Internal.Module;
import Ozone.Internal.Repo;
import Ozone.Internal.RepoCached;
import Ozone.Manifest;
import arc.util.Log;
import io.sentry.Sentry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Schematics implements Module {
	
	@Override
	public void loadAsync() {
		Repo rc = Manifest.getModule(Repo.class);
		assert rc != null;
		URL u = rc.getResource("src/schematic-pool.txt");
		if (u == null) throw new RuntimeException("Can't find src/schematic-pool.txt");
		try {
			HashMap<String, String> h = Encoder.parseProperty(u.openStream());
			HashMap<String, URL> list = new HashMap<>();
			for (Map.Entry<String, String> s : h.entrySet())
				list.put(s.getKey(), new URL(s.getValue()));
			for (Map.Entry<String, URL> s : list.entrySet())
				Pool.daemon(() -> {
				
				}).start();
		}catch (IOException e) {
			Log.err(e);
			Sentry.captureException(e);
		}
	}
	
	
	@Override
	public ArrayList<Class<? extends Module>> dependOnModule() {
		return new ArrayList<>(Arrays.asList(RepoCached.class));
	}
	
	public void init() {
	
	}
}