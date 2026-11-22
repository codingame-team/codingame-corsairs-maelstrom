<!-- LEAGUES level1 level2 level3 -->
<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">
  <!-- LEAGUE ALERT -->
  <!-- BEGIN level1 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Wood 2 League:</b> Command <b>1 Corsair Ship</b> with inertial physics and variable cargo mass. Collect floating treasure chests and deliver them to your Home Port!</p>
  </div>
  <!-- END -->
  <!-- BEGIN level2 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Wood 1 League:</b> You now command <b>2 Corsair Ships</b>, unlock the <action>ANCHOR</action> emergency brake, and the central <b>Maelstrom Vortex</b> is now active (radius <const>380</const>, lethal eye radius <const>65</const>)!</p>
  </div>
  <!-- END -->
  <!-- BEGIN level3 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Bronze+ League:</b> Unlock <b>Cannon Ballistics (<action>FIRE tx ty</action>)</b> (range <const>450</const>, blast radius <const>115</const>, cooldown <const>2</const>) to blast enemy ships into the Maelstrom and plunder up to <const>2</const> Gold per hit!</p>
  </div>
  <!-- END -->

  <!-- GOAL -->
  <div class="statement-section statement-goal">
    <h1><span class="icon icon-goal">&nbsp;</span><span>The Goal</span></h1>
    <div class="statement-goal-content">
      Be the first pirate fleet to deliver <const>35</const> <b>Gold</b> to your Home Port, or have the most delivered Gold after <const>120</const> turns.
    </div>
  </div>

  <!-- RULES -->
  <div class="statement-section statement-rules">
    <h1><span class="icon icon-rules">&nbsp;</span><span>Rules</span></h1>
    <div class="statement-rules-content">
      <p>The battle takes place in a <const>1600</const>&times;<const>900</const> sea sector with inertial kinematics, variable cargo mass, and rotational ocean currents:</p>
      <ul>
        <li><b>Inertial Sailing (<action>SAIL x y power</action>):</b> Applies thrust (<const>0</const> &le; <var>power</var> &le; <const>100</const>) toward (<var>x</var>, <var>y</var>). Heavier gold cargo increases ship mass and reduces acceleration:
          <br><code>accel = power / (1.0 + 0.15 * gold)</code></li>
        <!-- BEGIN level2 level3 -->
        <li><b>Emergency Brake (<action>ANCHOR</action>):</b> Immediately cuts current velocity by <const>75%</const> (<code>vx *= 0.25, vy *= 0.25</code>) and halves the pull of the Maelstrom for that turn.</li>
        <li><b>The Maelstrom Vortex:</b> Within <const>380</const> units of the center (<const>800</const>, <const>450</const>), a counter-clockwise swirl and inward suction vector are added to every ship's velocity each turn. Any ship ending its movement within <const>65</const> units of (<const>800</const>, <const>450</const>) is <b>shipwrecked</b>: it loses all carried gold and respawns at its Home Port!</li>
        <!-- END -->
        <li><b>Kinematics &amp; Water Drag:</b> Each turn, after thrust and currents are added to (<var>vx</var>, <var>vy</var>), the ship moves to (<var>x</var> + <var>vx</var>, <var>y</var> + <var>vy</var>), and water drag multiplies (<var>vx</var>, <var>vy</var>) by <const>0.78</const>.</li>
        <!-- BEGIN level3 -->
        <li><b>Cannons (<action>FIRE tx ty</action>):</b> Can be used standalone or appended to <action>SAIL</action> or <action>ANCHOR</action> when <var>cooldown</var> = <const>0</const> (range &le; <const>450</const>, sets <var>cooldown</var> to <const>2</const>). Any ship within <const>115</const> units of (<var>tx</var>, <var>ty</var>) is blasted away from the impact point and loses up to <const>2</const> carried Gold (plundered directly by the firing ship if it hit an enemy!).</li>
        <!-- END -->
        <li><b>Plunder &amp; Delivery:</b> Passing within <const>85</const> units of a Treasure Chest loads its Gold aboard. Entering within <const>130</const> units of your Home Port delivers all carried Gold to your score!</li>
      </ul>

      <!-- VICTORY CONDITIONS -->
      <div class="statement-victory-conditions">
        <div class="icon victory"></div>
        <div class="blk">
          <div class="title">Victory Conditions</div>
          <div class="text">
            <ul style="padding-bottom:0">
              <li>You are the first to deliver at least <const>35</const> Gold to your Home Port (with a strictly higher score than your opponent), or you have delivered more Gold after <const>120</const> turns.</li>
            </ul>
          </div>
        </div>
      </div>

      <!-- DEFEAT CONDITIONS -->
      <div class="statement-lose-conditions">
        <div class="icon lose"></div>
        <div class="blk">
          <div class="title">Defeat Conditions</div>
          <div class="text">
            <ul style="padding-bottom:0">
              <li>You have delivered less Gold than your opponent at the end of the game.</li>
              <li>Your program does not respond within the allotted time or outputs an unrecognized command.</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- PROTOCOL -->
  <div class="statement-section statement-protocol">
    <h1><span class="icon icon-protocol">&nbsp;</span><span>Game Input &amp; Output</span></h1>
    <div class="blk">
      <div class="title">Initialization Input</div>
      <div class="text">
        <p><span class="statement-lineno">Line 1:</span> 3 space-separated integers <var>width</var>, <var>height</var> and <var>leagueLevel</var>, the width and height of the sea sector (<const>1600</const>&times;<const>900</const>) and the current league level (<const>1</const>, <const>2</const> or <const>3</const>).</p>
        <p><span class="statement-lineno">Line 2:</span> 6 space-separated integers <var>myPortX</var>, <var>myPortY</var>, <var>oppPortX</var>, <var>oppPortY</var>, <var>maelstromX</var> and <var>maelstromY</var>, the coordinates of your Home Port, your opponent's Home Port, and the central Maelstrom.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Input for One Game Turn</div>
      <div class="text">
        <p><span class="statement-lineno">Line 1:</span> an integer <var>shipCount</var>, the total number of ships.</p>
        <p><span class="statement-lineno">Next <var>shipCount</var> lines:</span> 8 space-separated integers <var>shipId</var>, <var>owner</var>, <var>x</var>, <var>y</var>, <var>vx</var>, <var>vy</var>, <var>gold</var> and <var>cooldown</var>:</p>
        <ul>
          <li><var>shipId</var>: unique identifier of the ship.</li>
          <li><var>owner</var>: <const>0</const> if you own this ship (your ships are always listed first), <const>1</const> if owned by your opponent.</li>
          <li><var>x</var>, <var>y</var>: current coordinates of the ship.</li>
          <li><var>vx</var>, <var>vy</var>: current velocity components of the ship.</li>
          <li><var>gold</var>: amount of Gold currently carried aboard.</li>
          <li><var>cooldown</var>: turns remaining before cannons can fire again.</li>
        </ul>
        <p><span class="statement-lineno">Next line:</span> an integer <var>chestCount</var>, the number of active floating treasure chests.</p>
        <p><span class="statement-lineno">Next <var>chestCount</var> lines:</span> 4 space-separated integers <var>chestId</var>, <var>cx</var>, <var>cy</var> and <var>chestGold</var>, the unique identifier, coordinates, and Gold value of each chest.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Output for One Game Turn</div>
      <div class="text">
        <p><span class="statement-lineno">One line per owned ship</span> (in the exact order they appear in the input), containing one of the following commands:</p>
        <ul>
          <li><action>SAIL x y power</action> where <var>x</var> and <var>y</var> are integer target coordinates and <var>power</var> is an integer thrust from <const>0</const> to <const>100</const></li>
          <!-- BEGIN level2 level3 -->
          <li>or <action>ANCHOR</action> to deploy the emergency sea brake</li>
          <!-- END -->
          <!-- BEGIN level3 -->
          <li>optionally followed by <action>FIRE tx ty</action> (or standalone <action>FIRE tx ty</action>) where <var>tx</var> and <var>ty</var> are integer cannon target coordinates</li>
          <!-- END -->
        </ul>
        <!-- BEGIN level1 -->
        <p>You may optionally append <action>MSG text</action> at the end of a line. Example: <action>SAIL 800 450 90</action></p>
        <!-- END -->
        <!-- BEGIN level2 -->
        <p>You may optionally append <action>MSG text</action> at the end of a line. Example: <action>SAIL 800 450 90</action> or <action>ANCHOR</action></p>
        <!-- END -->
        <!-- BEGIN level3 -->
        <p>You may optionally append <action>MSG text</action> at the end of a line. Example: <action>SAIL 800 450 90 FIRE 750 400</action></p>
        <!-- END -->
      </div>
    </div>
    <div class="blk">
      <div class="title">Constraints</div>
      <div class="text">
        <var>width</var> = <const>1600</const>, <var>height</var> = <const>900</const><br>
        <const>2</const> &le; <var>shipCount</var> &le; <const>4</const><br>
        <const>0</const> &le; <var>chestCount</var> &le; <const>8</const><br>
        Response time for first turn &le; <const>1000</const> ms<br>
        Response time for one turn &le; <const>50</const> ms<br>
        Maximum number of turns: <const>120</const>
      </div>
    </div>
  </div>

  <!-- SOURCE CODE -->
  <div class="statement-section statement-examples">
    <h1><span class="icon icon-example">&nbsp;</span><span>Source Code</span></h1>
    <div class="statement-examples-text">
      The full referee, viewer, and starter bots source code is available on GitHub:
      <a href="https://github.com/codingame-team/codingame-corsairs-maelstrom" target="_blank" rel="noopener noreferrer">https://github.com/codingame-team/codingame-corsairs-maelstrom</a>
    </div>
  </div>
</div>
