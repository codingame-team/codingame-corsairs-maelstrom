<!-- LEAGUES level1 level2 level3 -->
<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">
  <!-- LEAGUE ALERT -->
  <!-- BEGIN level1 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bois 2 :</b> Contrôlez <b>1 Navire Corsaire</b> soumis à l'inertie et à la masse variable de sa cargaison. Récoltez les coffres flottants et rapportez-les à votre Port d'Attache !</p>
  </div>
  <!-- END -->
  <!-- BEGIN level2 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bois 1 :</b> Vous contrôlez désormais <b>2 Navires Corsaires</b>, débloquez le freinage d'urgence <action>ANCHOR</action>, et le <b>Maelström Central</b> est actif (rayon de courant <const>380</const>, œil mortel de rayon <const>65</const>) !</p>
  </div>
  <!-- END -->
  <!-- BEGIN level3 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bronze+ :</b> Débloquez la <b>Balistique des Canons (<action>FIRE tx ty</action>)</b> (portée <const>450</const>, rayon d'impact <const>115</const>, recharge <const>2</const> tours) pour projeter vos rivaux dans le Maelström et piller jusqu'à <const>2</const> Or par tir !</p>
  </div>
  <!-- END -->

  <!-- GOAL -->
  <div class="statement-section statement-goal">
    <h1><span class="icon icon-goal">&nbsp;</span><span>Objectif</span></h1>
    <div class="statement-goal-content">
      Soyez la première flotte pirate à livrer <const>35</const> <b>Or</b> à son Port d'Attache, ou ayez livré le plus d'Or à la fin des <const>120</const> tours.
    </div>
  </div>

  <!-- RULES -->
  <div class="statement-section statement-rules">
    <h1><span class="icon icon-rules">&nbsp;</span><span>Règles</span></h1>
    <div class="statement-rules-content">
      <p>La bataille se déroule dans un secteur maritime de <const>1600</const>&times;<const>900</const> avec cinématique inertielle, masse de cargaison variable et courants marins tourbillonnants :</p>
      <ul>
        <li><b>Navigation Inertielle (<action>SAIL x y power</action>) :</b> Applique une poussée (<const>0</const> &le; <var>power</var> &le; <const>100</const>) vers (<var>x</var>, <var>y</var>). Plus le navire transporte d'or, plus son inertie augmente :
          <br><code>accel = power / (1.0 + 0.15 * gold)</code></li>
        <!-- BEGIN level2 level3 -->
        <li><b>Ancre d'Urgence (<action>ANCHOR</action>) :</b> Réduit immédiatement la vitesse actuelle de <const>75%</const> (<code>vx *= 0.25, vy *= 0.25</code>) et divise par deux l'attraction du Maelström pendant ce tour.</li>
        <li><b>Le Vortex du Maelström :</b> À moins de <const>380</const> unités du centre (<const>800</const>, <const>450</const>), un courant tangentiel anti-horaire et une aspiration radiale s'ajoutent à la vitesse des navires. Tout navire terminant son déplacement à moins de <const>65</const> unités de (<const>800</const>, <const>450</const>) fait <b>naufrage</b> : il perd toute sa cargaison d'or et réapparaît à son Port d'Attache !</li>
        <!-- END -->
        <li><b>Cinématique &amp; Frottement de l'Eau :</b> À chaque tour, après l'ajout de la poussée et des courants à (<var>vx</var>, <var>vy</var>), le navire avance en (<var>x</var> + <var>vx</var>, <var>y</var> + <var>vy</var>), puis la friction de l'eau multiplie (<var>vx</var>, <var>vy</var>) par <const>0.78</const>.</li>
        <!-- BEGIN level3 -->
        <li><b>Canons (<action>FIRE tx ty</action>) :</b> Utilisable seul ou combiné après <action>SAIL</action> ou <action>ANCHOR</action> lorsque <var>cooldown</var> = <const>0</const> (portée &le; <const>450</const>, passe <var>cooldown</var> à <const>2</const>). Tout navire situé à moins de <const>115</const> unités de (<var>tx</var>, <var>ty</var>) subit une onde de choc répulsive et perd jusqu'à <const>2</const> Or de sa cargaison (pillés directement par le tireur s'il s'agit d'un navire ennemi !).</li>
        <!-- END -->
        <li><b>Collecte &amp; Livraison :</b> Passer à moins de <const>85</const> unités d'un coffre charge son Or à bord. Entrer à moins de <const>130</const> unités de votre Port d'Attache décharge tout l'Or transporté dans votre score !</li>
      </ul>

      <!-- VICTORY CONDITIONS -->
      <div class="statement-victory-conditions">
        <div class="icon victory"></div>
        <div class="blk">
          <div class="title">Conditions de Victoire</div>
          <div class="text">
            <ul style="padding-bottom:0">
              <li>Vous êtes le premier à livrer au moins <const>35</const> Or à votre Port d'Attache (avec un score strictement supérieur à votre adversaire), ou vous avez livré plus d'Or après <const>120</const> tours.</li>
            </ul>
          </div>
        </div>
      </div>

      <!-- DEFEAT CONDITIONS -->
      <div class="statement-lose-conditions">
        <div class="icon lose"></div>
        <div class="blk">
          <div class="title">Conditions de Défaite</div>
          <div class="text">
            <ul style="padding-bottom:0">
              <li>Vous avez livré moins d'Or que votre adversaire à la fin de la partie.</li>
              <li>Votre programme ne répond pas dans le temps imparti ou affiche une commande non reconnue.</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- PROTOCOL -->
  <div class="statement-section statement-protocol">
    <h1><span class="icon icon-protocol">&nbsp;</span><span>Entrées &amp; Sorties du Jeu</span></h1>
    <div class="blk">
      <div class="title">Entrées d'Initialisation</div>
      <div class="text">
        <p><span class="statement-lineno">Ligne 1 :</span> 3 entiers séparés par des espaces <var>width</var>, <var>height</var> et <var>leagueLevel</var>, la largeur et la hauteur du secteur maritime (<const>1600</const>&times;<const>900</const>) et le niveau de la ligue (<const>1</const>, <const>2</const> ou <const>3</const>).</p>
        <p><span class="statement-lineno">Ligne 2 :</span> 6 entiers séparés par des espaces <var>myPortX</var>, <var>myPortY</var>, <var>oppPortX</var>, <var>oppPortY</var>, <var>maelstromX</var> et <var>maelstromY</var>, les coordonnées de votre Port d'Attache, de celui de l'adversaire et du Maelström central.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Entrées pour un Tour de Jeu</div>
      <div class="text">
        <p><span class="statement-lineno">Ligne 1 :</span> un entier <var>shipCount</var>, le nombre total de navires.</p>
        <p><span class="statement-lineno">Les <var>shipCount</var> lignes suivantes :</span> 8 entiers séparés par des espaces <var>shipId</var>, <var>owner</var>, <var>x</var>, <var>y</var>, <var>vx</var>, <var>vy</var>, <var>gold</var> et <var>cooldown</var> :</p>
        <ul>
          <li><var>shipId</var> : identifiant unique du navire.</li>
          <li><var>owner</var> : <const>0</const> si vous possédez ce navire (vos navires sont toujours listés en premier), <const>1</const> s'il appartient à l'adversaire.</li>
          <li><var>x</var>, <var>y</var> : coordonnées actuelles du navire.</li>
          <li><var>vx</var>, <var>vy</var> : composantes de vitesse actuelles du navire.</li>
          <li><var>gold</var> : quantité d'Or actuellement transportée à bord.</li>
          <li><var>cooldown</var> : nombre de tours restants avant que les canons puissent tirer à nouveau.</li>
        </ul>
        <p><span class="statement-lineno">Ligne suivante :</span> un entier <var>chestCount</var>, le nombre de coffres flottants actifs.</p>
        <p><span class="statement-lineno">Les <var>chestCount</var> lignes suivantes :</span> 4 entiers séparés par des espaces <var>chestId</var>, <var>cx</var>, <var>cy</var> et <var>chestGold</var>, l'identifiant unique, les coordonnées et la valeur en Or de chaque coffre.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Sortie pour un Tour de Jeu</div>
      <div class="text">
        <p><span class="statement-lineno">Une ligne par navire que vous possédez</span> (dans l'ordre exact où ils apparaissent en entrée) :</p>
        <ul>
          <li><action>SAIL x y power</action> où <var>x</var> et <var>y</var> sont des coordonnées entières et <var>power</var> une poussée entière entre <const>0</const> et <const>100</const></li>
          <!-- BEGIN level2 level3 -->
          <li>ou <action>ANCHOR</action> pour déployer l'ancre de freinage d'urgence</li>
          <!-- END -->
          <!-- BEGIN level3 -->
          <li>éventuellement suivi de <action>FIRE tx ty</action> (ou <action>FIRE tx ty</action> seul) où <var>tx</var> et <var>ty</var> sont les coordonnées entières visées par vos canons</li>
          <!-- END -->
        </ul>
        <p>Vous pouvez ajouter <action>MSG texte</action> en fin de ligne. Exemple : <action>SAIL 800 450 90 FIRE 750 400</action></p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Contraintes</div>
      <div class="text">
        <var>width</var> = <const>1600</const>, <var>height</var> = <const>900</const><br>
        <const>2</const> &le; <var>shipCount</var> &le; <const>4</const><br>
        <const>0</const> &le; <var>chestCount</var> &le; <const>8</const><br>
        Temps de réponse pour le premier tour &le; <const>1000</const> ms<br>
        Temps de réponse par tour &le; <const>50</const> ms<br>
        Nombre maximum de tours : <const>120</const>
      </div>
    </div>
  </div>

  <!-- CODE SOURCE -->
  <div class="statement-section statement-examples">
    <h1><span class="icon icon-example">&nbsp;</span><span>Code Source</span></h1>
    <div class="statement-examples-text">
      Le code source complet de l'arbitre (Referee), du viewer et des bots de référence est disponible sur GitHub :
      <a href="https://github.com/codingame-team/codingame-corsairs-maelstrom" target="_blank" rel="noopener noreferrer">https://github.com/codingame-team/codingame-corsairs-maelstrom</a>
    </div>
  </div>
</div>
