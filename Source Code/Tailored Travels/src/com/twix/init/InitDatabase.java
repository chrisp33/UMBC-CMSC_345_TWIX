package com.twix.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitDatabase {
	public static final String newDatabase = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String url = "jdbc:derby:Database;create = true";

	/**
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {
		try {
			CreateWaypointTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// Already created
		}
		try {
			CreateUserTable();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// Already Created
		}
	}

	public static void CreateWaypointTable() throws SQLException,
			ClassNotFoundException {
		Class.forName(newDatabase);
		// creates the database if it doesn't exist
		Connection connect = DriverManager.getConnection(url);
		// creates the db_waypoints table in database
		connect.createStatement()
				.execute(
						"CREATE TABLE db_waypoints ("
								+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
								+ "PRIMARY KEY (id),"
								+ "name varchar(40) not null, "
								+ "latitude decimal(7, 4) not null, "
								+ "longitude decimal(7, 4) not null,"
								+ "description varchar(500) not null)");

		// adds default db_waypointss
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Acadia', 44.35, -68.2167,'Covering most of Mount Desert Island and other coastal islands, Acadia features the tallest mountain on the Atlantic coast, granite peaks, ocean shoreline, woodlands, and lakes. There are freshwater, estuary, forest, and intertidal habitats')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('American Samoa', -14.25, -170.68,'The southernmost national park is on three Samoan islands and protects coral reefs, rainforests, volcanic mountains, and white beaches. The area is also home to flying foxes, brown boobies, sea turtles, and 900 species of fish')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Arches', 38.68, -109.57,'This site features more than 2,000 natural sandstone arches, including the Delicate Arch. In a desert climate millions of years of erosion have led to these structures, and the arid ground has life-sustaining soil crust and potholes, natural water-collecting basins. Other geologic formations are stone columns, spires, fins, and towers')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Badlands', 43.75, -102.5,'The Badlands are a collection of buttes, pinnacles, spires, and grass prairies. It has the world''s richest fossil beds from the Oligocene epoch, and there is wildlife including bison, bighorn sheep, black-footed ferrets, and swift foxes')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Big Bend', 29.25, -103.25,'Named for the Bend of the Rio Grande along the US�Mexico border, this park includes a part of the Chihuahuan Desert. A wide variety of Cretaceous and Tertiary fossils as well as cultural artifacts of Native Americans exist within its borders')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Biscayne', 25.65, 80.08,'Located in Biscayne Bay, this park at the north end of the Florida Keys has four interrelated marine ecosystems: mangrove forest, the Bay, the Keys, and coral reefs. Threatened animals include the West Indian Manatee, American crocodile, various sea turtles, and peregrine falcon')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Black Canyon of Guinnson', 38.57, -107.72,'The park protects a quarter of the Gunnison River, which has dark canyon walls from the Precambrian era. The canyon has very steep descents, and it is a site for river rafting and rock climbing. The narrow, steep canyon, made of gneiss and schist, is often in shadow, appearing black.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Bryce Canyon', 37.57, -112.18,'Bryce Canyon is a giant natural amphitheatre along the Paunsaugunt Plateau. The unique area has hundreds of tall hoodoos formed by erosion. The region was originally settled by Native Americans and later by Mormon pioneers.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Canyonlands', 38.2, -109.93,'This landscape was eroded into canyons, buttes, and mesas by the Colorado River, Green River, and their tributaries, which divide the park into three districts. There are rock pinnacles and other naturally sculpted rock, as well as artifacts from Ancient Pueblo Peoples.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Capitol Reef', 38.2, -111.17,'The park''s Waterpocket Fold is a 100-mile (160 km) monocline that shows the Earth''s geologic layers. Other natural features are monoliths and sandstone domes and cliffs shaped like the United States Capitol.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Carlsbad Caverns', 32.17, -104.44,'Carlsbad Caverns has 117 caves, the longest of which is over 120 miles (190 km) long. The Big Room is almost 4,000 feet (1,200 m) long, and the caves are home to over 400,000 Mexican Free-tailed Bats and sixteen other species. Above ground are the Chihuahuan Desert and Rattlesnake Springs.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Channel Islands', 34.01, -119.42,'Five of the eight Channel Islands are protected, and half of the park''s area is underwater. The islands have a unique Mediterranean ecosystem. They are home to over 2,000 species of land plants and animals, and 145 are unique to them. The islands were originally settled by the Chumash people')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Congaree', 33.78, -80.78,'On the Congaree River, this park is the largest portion of old-growth floodplain forest left in North America. Some of the trees are the tallest in the Eastern US, and the Boardwalk Loop is an elevated walkway through the swamp.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Crater Lake', 42.94, -122.1,'Crater Lake lies in the caldera of Mount Mazama formed 7,700 years ago after an eruption. It is the deepest lake in the United States and is known for its blue color and water clarity. There are two islands in the lake, and, with no inlets or outlets, all water comes through precipitation.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Cuyahoga Valley', 41.24, -81.55,'This park along the Cuyahoga River has waterfalls, hills, trails, and displays about early rural living. The Ohio and Erie Canal Towpath Trail follows the Ohio and Erie Canal, where mules towed canal boats. The park has numerous historic homes, bridges, and structures.[20] The park also offers a scenic train ride with various trips available.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Death Valley', 36.24, -116.82,'Death Valley is the hottest, lowest, and driest place in the United States. Daytime temperatures have topped 130�F (54�C) and it is home to Badwater Basin, the lowest point in North America. There are canyons, colorful badlands, sand dunes, mountains, and over 1000 species of plants in this graben on a fault line. Further geologic points of interest are salt flats, springs, and buttes.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Denali', 63.33, -150.5,'Centered around the Mount McKinley, the tallest mountain in North America, Denali is serviced by a single road leading to Wonder Lake. McKinley and other peaks of the Alaska Range are covered with long glaciers and boreal forest. Wildlife includes grizzly bears, Dall sheep, caribou, and gray wolves.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Dry Tortugas', 24.63, -82.87,'The Dry Tortugas on the west end of the Florida Keys are the site of Fort Jefferson, the largest masonry structure in the Western Hemisphere. With most of the park being water, it is the home of coral reefs and shipwrecks and is only accessible by plane or boat.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Everglades', 25.32, -80.93,'The Everglades are the largest subtropical wilderness in the United States. This mangrove ecosystem and marine estuary is home to 36 protected species, including the Florida panther, American crocodile, and West Indian manatee. Some areas have been drained and developed; restoration projects aim to restore the ecology.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Gates of the Arctic', 67.78, -153.3,'This northernmost park protects part of the Brooks Range and has no park facilities. The land is home to Alaska natives, who have relied on the land and caribou for 11,000 years.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Glacier', 48.8, -114.0,'Part of Waterton Glacier International Peace Park, this park has 26 remaining glaciers and 130 named lakes under the tall Rocky Mountain peaks. There are historic hotels and a landmark road in this region of rapidly receding glaciers. These mountains, formed by an overthrust, have the world\''s best sedimentary fossils from the Proterozoic era.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Glacier Bay', 58.5, -137.0,'Glacier Bay has numerous tidewater glaciers, mountains, and fjords. The temperate rainforest and the bay are home to grizzly bears, mountain goats, whales, seals, and eagles. When discovered in 1794 by George Vancouver, the entire bay was covered by ice, but the glaciers have receded over 65 miles (105 km).')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Grand Canyon', 36.06, -112.14,'The Grand Canyon, carved out by the Colorado River, is 277 miles (446 km) long, up to 1 mile (1.6 km) deep, and up to 15 miles (24 km) wide. Millions of years of exposure has formed colorful layers of the Colorado Plateau in mesas and canyon walls.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Grand Teton', 43.73, -110.8,'Grand Teton is the tallest mountain in the Teton Range. The park''s Jackson Hole valley and reflective piedmont lakes contrast with the tall mountains, which abruptly rise from the sage-covered valley.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Great Basin', 38.98, -114.3,'Based around Wheeler Peak, the Great Basin has 5,000-year-old bristlecone pines, glacial moraines, and the limestone Lehman Caves. It has some of the country''s darkest night skies, and there are animal species including Townsend''s big-eared bat, Pronghorn, and Bonneville cutthroat trout.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Great Sand Dunes', 37.73, -105.51,'The tallest dunes in North America are up to 750 feet (230 m) tall and neighbor grasslands, shrublands and wetlands. They were formed by sand deposits of the Rio Grande on the San Luis Valley. The park also has alpine lakes, six 13,000-foot mountains, and ancient forests.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Great Smoky Mountains', 35.68, -83.53,'The Great Smoky Mountains, part of the Appalachian Mountains, have a wide range of elevations, making them home to over 400 vertebrate species, 100 tree species, and 5000 plant species. Hiking is the park''s main attraction, with over 800 miles (1,300 km) of trails, including 70 miles (110 km) of the Appalachian Trail. Other activities are fishing, horseback riding, and visiting some of nearly 80 historic structures.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Guadalupe Mountains', 31.92, -104.87,'This park has Guadalupe Peak, the highest point in Texas, the scenic McKittrick Canyon full of Bigtooth Maples, part of the Chihuahuan Desert, and a fossilized reef from the Permian.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Haleakala', 20.72, -156.17,'The Haleakala volcano on Maui has a very large crater with many cinder cones, Hosmer''s Grove of alien trees, and the native Hawaiian Goose. The Kipahulu section has numerous pools with freshwater fish. This National Park has the greatest number of endangered species.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Hawaii Volcanoes', 19.38, -155.2,'This park on the Big Island protects the Kilauea and Mauna Loa volcanoes, two of the world''s most active. Diverse ecosystems of the park range from those at sea level to 13,000 feet (4,000 m).')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Hot Springs', 34.51, -93.05,'The only National Park in an urban area, this smallest National Park is based around the natural hot springs that have been managed for public use. Bathhouse Row preserves 47 of these with many beneficial minerals.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Isle Royale', 48.1, -88.55,'The largest island in Lake Superior, this park is a site of isolation and wilderness. It has many shipwrecks, waterways, and hiking trails. The park also includes over 400 smaller islands in the waters up to 4.5 miles (7.2 km) from the island. There are only 20 mammal species and it is known for its wolf and moose relationship.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Joshua Tree', 33.79, -115.9,'Covering parts of the Colorado and Mojave Deserts and the Little San Bernardino Mountains, this is the home of the Joshua tree. Across great elevation changes are sand dunes, dry lakes, rugged mountains, and granite monoliths.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Katmai', 58.5, -155.0,'This park on the Alaska Peninsula protects the Valley of Ten Thousand Smokes, an ash flow formed by the 1912 eruption of Novarupta, as well as Mount Katmai. Over 2,000 brown bears come here to catch spawning salmon.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Kenai Fjords', 59.95, -149.65,'Near Seward on the Kenai Peninsula, this park protects the Harding Icefield and at least 38 glaciers and fjords stemming from it. The only area accessible to the public by road is Exit Glacier, while the rest can only be viewed by boat tours.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Kings Canyon', 36.8, -118.55,'Home to several Giant sequoia groves and the General Grant Tree, the world''s second largest, this park also has part of the Kings River, site of the granite Kings Canyon, and San Joaquin River, as well as the Boyden Cave.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Kobuk Valley', 67.55, -159.28,'Kobuk Valley has 61 miles (98 km) of the Kobuk River and three regions of sand dunes. Created by glaciers, the Great Kobuk, the Little Kobuk, and the Hunt River Sand Dunes can reach 100 feet (30 m) high and 100 �F (38 �C), and they are the largest dunes in the arctic. Twice a year, half a million caribou migrate through the dunes and across river bluffs that contain ice age fossils.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Lake Clark', 60.97, -153.42,'The region around Lake Clark has four active volcanoes, including Mount Redoubt, rivers, glaciers, and waterfalls. There are temperate rainforests, a tundra plateau, and three mountain ranges.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Lassen Vocanic', 40.49, -121.51,'Lassen Peak, the largest plug dome volcano in the world, is joined by all three other types of volcanoes in this park: shield, cinder dome, and composite. Other than the volcano, which last erupted in 1915, the park has hydrothermal areas, including fumaroles, boiling pools, and steaming ground, heated by molten rock under the peak.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Machu Pichu', -13.1631, 72.5456,'Machu Picchu or Machu Pikchu is a 15th-century Inca site located 2,430 metres above sea level. It is located in the Cusco Region, Urubamba Province, Machupicchu District in Peru.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Mammoth Cave', 37.18, -86.1,'With 392 miles (631 km) of passageways mapped, Mammoth Cave is by far the world''s longest cave system. Cave animals include eight bat species, Kentucky cave shrimp, Northern cavefish, and cave salamanders. Above ground, the park contains Green River (Kentucky), 70 miles of hiking trails, sinkholes, and springs.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Mesa Verde', 37.18, -108.49,'This area has over 4,000 archaeological sites of the Ancestral Pueblo, who lived here for 700 years. Cliff dwellings built in the 12th and 13th centuries include Cliff Palace, which has 150 rooms and 23 kivas, and the Balcony House, with passages and tunnels.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Mount Rainier', 46.85, -121.75,'Mount Rainier, an active volcano, is the most prominent peak in the Cascades, and it is covered by 26 named glaciers including Carbon Glacier and Emmons Glacier, the largest in the continental United States. The mountain is popular for climbing, and more than half of the park is covered by subalpine and alpine forests. Paradise on the south slope is one of the snowiest places in the world, and the Longmire visitor center is the start of the Wonderland Trail, which encircles the mountain.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('North Cascades', 48.7, -121.2,'This complex includes the two units of the National Park and the Ross Lake and Lake Chelan National Recreation Areas. There are numerous glaciers, and popular hiking and climbing areas are Cascade Pass, Mount Shuksan, Mount Triumph, and Eldorado Peak.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Olympic', 47.97, -123.5,'situated on the Olympic Peninsula, this park ranges from Pacific shoreline with tide pools to temperate rainforests to Mount Olympus. The glaciated Olympic Mountains overlook the Hoh Rain Forest and Quinault Rain Forest, the wettest area of the continental United States.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Petrified Forest', 35.07, -109.78,'This portion of the Chinle Formation has a great concentration of 225-million-year-old petrified wood. The surrounding region, the Painted Desert, has eroded red-hued volcanic rock called bentonite. There are also dinosaur fossils and over 350 Native American sites.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Pinnacles', 36.48, -121.16,'Known for the namesake eroded leftovers of half of an extinct volcano, it is popular for its rock climbing.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Redwood', 41.3, -124.0,'This park and the co-managed state parks protect almost half of all remaining Coastal Redwoods, the tallest trees on Earth. There are three large river systems in this very seismically active area, and the 37 miles (60 km) of protected coastline have tide pools and seastacks. The prairie, estuary, coast, river, and forest ecosystems have varied animal and plant species.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Rocky Mountain', 40.4, -105.58,'This section of the Rocky Mountains has ecosystems varying in elevation from the over 150 riparian lakes to Montane and subalpine forests to the alpine tundra. Large wildlife including mule deer, bighorn sheep, black bears, and cougars inhabit these igneous mountains and glacier valleys. The fourteener Longs Peak and Bear Lake are popular destinations.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('saguaro', 32.25, -110.5,'split into the separate Rincon Mountain and Tucson Mountain Districts, the dry Sonoran Desert is still home to much life in six biotic communities. Beyond the namesake Giant Saguaro cacti, there are barrel cacti, cholla cacti, and prickly pears, as well as Lesser Long-nosed Bats, Spotted Owls, and javelinas.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('sequoia', 36.43, -118.68,'This park protects the Giant Forest, which has the world''s largest tree, General Sherman, as well as four of the next nine. It also has over 240 caves, the tallest mountain in the continental U.S., Mount Whitney, and the granite dome Moro Rock.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('shenandoah', 38.53, -78.35,'shenandoah''s Blue Ridge Mountains are covered by hardwood forests that are home to tens of thousands of animals. The Skyline Drive and Appalachian Trail run the entire length of this narrow park that has more than 500 miles (800 km) of hiking trails along scenic overlooks and waterfalls of the Shenandoah River.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Theodore Roosevelt', 46.97, -103.45,'This region that enticed and influenced President Theodore Roosevelt is now a park of three units in the badlands. Besides Roosevelt''s historic cabin, there are scenic drives and backcountry hiking opportunities. Wildlife includes American Bison, pronghorns, Bighorn sheep, and wild horses.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Virgin Islands', 18.33, -64.73,'The island of Saint John has rich human and natural history. There are Taino archaeological sites and ruins of sugar plantations from Columbus''s time. Past the pristine beaches are mangroves, seagrass beds, coral reefs and algal plains.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Voyageurs', 48.5, -92.88,'This park on four main lakes, a site for canoeing, kayaking, and fishing, has a history of Ojibwe Native Americans, French fur traders called voyageurs, and a gold rush. Formed by glaciers, this region has tall bluffs, rock gardens, islands and bays, and historic buildings.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Wind Cave', 43.57, -103.48,'Wind Cave is distinctive for its calcite fin formations called boxwork and needle-like growths called frostwork. The cave, which was discovered by the sound of wind coming from a hole in the ground, is the world''s densest cave system. Above ground is a mixed-grass prairie with animals such as bison, black-footed ferrets, and prairie dogs,and Ponderosa pine forests home to cougars and elk.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Wrangell -St. Elias', 61.0, -142.0,'This mountainous land has the convergence of the Alaska, Chugach, and Wrangell-Saint Elias Ranges, which have many of the continent''s tallest mountains over 16,000 feet (4,900 m), including Mount Saint Elias. More than 25% of this park of volcanic peaks is covered with glaciers, including the tidewater Hubbard Glacier, piedmont Malaspina Glacier, and valley Nabesna Glacier.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('YellowStone', 44.6, -110.5,'situated on the Yellowstone Caldera, the first national park in the world has vast geothermal areas such as hot springs and geysers, the best-known being Old Faithful and Grand Prismatic Spring. The yellow-hued Grand Canyon of the Yellowstone River has numerous waterfalls, and four mountain ranges run through the park. There are almost 60 mammal species, including the gray wolf, grizzly bear, lynx, bison, and elk.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Yosemite', 37.83, -119.5,'Yosemite has towering cliffs, waterfalls, and sequoias in a diverse area of geology and hydrology. Half Dome and El Capitan rise from the central glacier-formed Yosemite Valley, as does Yosemite Falls, North America''s tallest waterfall. Three Giant Sequoia groves and vast wilderness are home to diverse wildlife.')");
		connect.createStatement()
				.execute(
						"insert into db_waypoints (name, latitude, longitude, description) values ('Zion', 37.3, -113.05,'This geologically unique area has colorful sandstone canyons, high plateaus, and rock towers. Natural arches and exposed formations of the Colorado Plateau make up a large wilderness of four ecosystems.')");

		connect.close();
		System.out.println("Code finished");
	}

	public static void CreateUserTable() throws SQLException,
			ClassNotFoundException {
		Class.forName(newDatabase);
		// create the database if it doesn't exist
		Connection connect = DriverManager.getConnection(url);
		// creates the location table in database
		connect.createStatement()
				.execute(
						"create table db_users ("
								+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
								+ "PRIMARY KEY (id),"
								+ "name varchar(20) not null,"
								+ "password varchar(20) not null,"
								+ "admin boolean not null)");

		connect.createStatement()
				.execute(
						"insert into db_users (name, password, admin) values ('Keith', 'password1', true)");
		connect.createStatement()
				.execute(
						"insert into db_users (name, password, admin) values ('Stephen' , 'pw2', true)");
		connect.createStatement()
				.execute(
						"insert into db_users (name, password, admin) values ('Chris', 'password3',false)");
		connect.close();
		System.out.println("Code finished");
	}

}
