<!-- LEAGUES level1 level2 level3 -->
<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">
  <!-- LEAGUE ALERT -->
  <!-- BEGIN level1 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bois 3 :</b> Commandez <b>1 Navire Corsaire</b> soumis à la physique inertielle et à la masse de sa cargaison. Récupérez les coffres flottants et livrez-les à votre Port !</p>
  </div>
  <!-- END -->
  <!-- BEGIN level2 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bois 2 :</b> Vous commandez désormais <b>2 Navires Corsaires</b>, débloquez le frein d'urgence <action>ANCHOR</action>, et le <b>Vortex du Maelström</b> central s'active (rayon <const>380</const>, œil mortel de rayon <const>65</const>) !</p>
  </div>
  <!-- END -->
  <!-- BEGIN level3 -->
  <div style="color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-bottom: 15px; text-align: left">
    <p style="margin-bottom: 0"><b>Ligue Bois 1 :</b> Débloquez la <b>Balistique des Canons (<action>FIRE</action>)</b> (portée <const>450</const>, rayon d'explosion <const>115</const>, recharge <const>2</const> tours) pour propulser les ennemis dans le Maelström et piller jusqu'à <const>2</const> Or par tir !</p>
  </div>
  <!-- END -->

  <!-- GOAL -->
  <div class="statement-section statement-goal">
    <h1><span class="icon icon-goal">&nbsp;</span><span>Objectif</span></h1>
    <div class="statement-goal-content">
      Soyez la première flotte pirate à livrer <const>35</const> pièces d'<b>Or</b> dans votre Port, ou possédez le plus d'Or livré au terme des <const>120</const> tours.
    </div>
  </div>

  <!-- RULES -->
  <div class="statement-section statement-rules">
    <h1><span class="icon icon-rules">&nbsp;</span><span>Règles</span></h1>
    <div class="statement-rules-content">
      <!-- BEGIN level1 -->
      <p>La bataille navale se déroule dans un secteur maritime de <const>1600</const>&times;<const>900</const> à symétrie centrale, avec cinématique inertielle et masse de cargaison variable. Les coordonnées suivent la convention standard : <var>x</var> croît vers la droite, <var>y</var> croît vers le bas et <const>(0, 0)</const> correspond au coin supérieur gauche.</p>
      <!-- END -->
      <!-- BEGIN level2 level3 -->
      <p>La bataille navale se déroule dans un secteur maritime de <const>1600</const>&times;<const>900</const> à symétrie centrale, avec cinématique inertielle, masse de cargaison variable et courants marins rotatifs. Les coordonnées suivent la convention standard : <var>x</var> croît vers la droite, <var>y</var> croît vers le bas et <const>(0, 0)</const> correspond au coin supérieur gauche.</p>
      <!-- END -->

      <h3 style="font-size: 16px; font-weight: 700; padding-top: 10px">Ordre de Résolution d'un Tour</h3>
      <ol>
        <li><b>Navigation Inertielle (<action>SAIL</action> <var>x</var> <var>y</var> <var>power</var>) :</b> Applique une poussée (<const>0</const> &le; <var>power</var> &le; <const>100</const>) vers (<var>x</var>, <var>y</var>). Plus la cargaison d'or est lourde, plus l'accélération diminue :
          <br><code>accel = power / (1.0 + 0.15 * gold)</code></li>
        <!-- BEGIN level2 level3 -->
        <li><b>Frein d'Urgence (<action>ANCHOR</action>) :</b> Réduit immédiatement la vitesse courante de <const>75%</const> (<code>vx *= 0.25, vy *= 0.25</code>) et divise par deux l'attraction du Maelström pour ce tour.</li>
        <li><b>Vortex du Maelström :</b> Dans un rayon de <const>380</const> unités autour du centre (<const>800</const>, <const>450</const>), un courant tourbillonnant anti-horaire et une aspiration centripète s'ajoutent à la vitesse de chaque navire. Tout navire terminant son mouvement à moins de <const>65</const> unités de (<const>800</const>, <const>450</const>) fait <b>naufrage</b> : il perd tout son or embarqué et réapparaît à son Port !</li>
        <!-- END -->
        <!-- BEGIN level1 -->
        <li><b>Cinématique &amp; Traînée Marine :</b> À chaque tour, après ajout de la poussée à (<var>vx</var>, <var>vy</var>), le navire se déplace en (<var>x</var> + <var>vx</var>, <var>y</var> + <var>vy</var>), puis la traînée de l'eau multiplie (<var>vx</var>, <var>vy</var>) par <const>0.78</const>.</li>
        <!-- END -->
        <!-- BEGIN level2 level3 -->
        <li><b>Cinématique &amp; Traînée Marine :</b> À chaque tour, après ajout de la poussée et des courants à (<var>vx</var>, <var>vy</var>), le navire se déplace en (<var>x</var> + <var>vx</var>, <var>y</var> + <var>vy</var>), puis la traînée de l'eau multiplie (<var>vx</var>, <var>vy</var>) par <const>0.78</const>.</li>
        <!-- END -->
        <!-- BEGIN level3 -->
        <li><b>Canons (<action>FIRE</action> <var>tx</var> <var>ty</var>) :</b> Utilisable seul ou à la suite de <action>SAIL</action> / <action>ANCHOR</action> lorsque <var>cooldown</var> = <const>0</const> (portée euclidienne depuis le navire tireur &le; <const>450</const>, passe <var>cooldown</var> à <const>2</const>). Tout navire à moins de <const>115</const> unités de (<var>tx</var>, <var>ty</var>) subit une onde de choc et perd jusqu'à <const>2</const> pièces d'Or (immédiatement pillées par le navire tireur s'il s'agit d'un ennemi !).</li>
        <!-- END -->
        <li><b>Pillage &amp; Livraison :</b> Passer à moins de <const>85</const> unités d'un Coffre au Trésor charge son Or à bord. Entrer à moins de <const>130</const> unités de votre Port livre tout votre Or embarqué à votre score !</li>
      </ol>
      <!-- BEGIN level1 level2 -->
      <p><b>Actions inexécutables :</b> Les valeurs de <var>power</var> hors bornes sont ramenées dans [<const>0</const>, <const>100</const>], et les positions sont maintenues dans les limites du secteur. Les commandes syntaxiquement valides n'entraînent pas la défaite.</p>
      <!-- END -->
      <!-- BEGIN level3 -->
      <p><b>Actions inexécutables :</b> Les valeurs de <var>power</var> hors bornes sont ramenées dans [<const>0</const>, <const>100</const>], et les commandes <action>FIRE</action> envoyées avec <var>cooldown</var> &gt; <const>0</const> ou au-delà de la portée <const>450</const> sont simplement ignorées et n'entraînent pas la défaite.</p>
      <!-- END -->

      <!-- VICTORY CONDITIONS -->
      <div class="statement-victory-conditions">
        <div class="icon victory"></div>
        <div class="blk">
          <div class="title">Conditions de Victoire</div>
          <div class="text">
            <ul style="padding-bottom:0">
              <li>Vous êtes le premier à livrer au moins <const>35</const> Or à votre Port (avec un score strictement supérieur à votre adversaire), ou vous avez livré plus d'Or au bout des <const>120</const> tours.</li>
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
              <li>Votre programme ne répond pas dans le temps imparti ou affiche une commande mal formée / non reconnue.</li>
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
        <p><span class="statement-lineno">Ligne 1 :</span> 3 entiers séparés par des espaces <var>width</var>, <var>height</var> et <var>leagueLevel</var>, la largeur et la hauteur du secteur maritime (<const>1600</const>&times;<const>900</const>) ainsi que la division Bois actuelle (<const>3</const> pour Bois 3, <const>2</const> pour Bois 2, <const>1</const> pour Bois 1).</p>
        <p><span class="statement-lineno">Ligne 2 :</span> 6 entiers séparés par des espaces <var>myPortX</var>, <var>myPortY</var>, <var>oppPortX</var>, <var>oppPortY</var>, <var>maelstromX</var> et <var>maelstromY</var>, les coordonnées de votre Port, du Port adverse et du Maelström central.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Entrées pour un Tour de Jeu</div>
      <div class="text">
        <p><span class="statement-lineno">Ligne 1 :</span> un entier <var>turn</var>, le numéro du tour courant (<const>1</const> à <const>120</const>).</p>
        <p><span class="statement-lineno">Ligne suivante :</span> un entier <var>shipCount</var>, le nombre total de navires.</p>
        <p><span class="statement-lineno">Les <var>shipCount</var> lignes suivantes :</span> 8 entiers séparés par des espaces <var>shipId</var>, <var>owner</var>, <var>x</var>, <var>y</var>, <var>vx</var>, <var>vy</var>, <var>gold</var> et <var>cooldown</var> :</p>
        <ul>
          <li><var>shipId</var> : identifiant unique du navire.</li>
          <li><var>owner</var> : <const>0</const> si le navire vous appartient (vos navires sont toujours listés en premier), <const>1</const> s'il appartient à votre adversaire.</li>
          <li><var>x</var>, <var>y</var> : coordonnées actuelles du navire.</li>
          <li><var>vx</var>, <var>vy</var> : composantes de la vitesse actuelle du navire.</li>
          <li><var>gold</var> : quantité d'Or actuellement embarquée à bord.</li>
          <!-- BEGIN level1 level2 -->
          <li><var>cooldown</var> : vaut toujours <const>0</const> dans cette division, les canons n'étant pas disponibles.</li>
          <!-- END -->
          <!-- BEGIN level3 -->
          <li><var>cooldown</var> : nombre de tours restants avant de pouvoir tirer à nouveau au canon.</li>
          <!-- END -->
        </ul>
        <p><span class="statement-lineno">Ligne suivante :</span> un entier <var>chestCount</var>, le nombre de coffres au trésor flottants actifs.</p>
        <p><span class="statement-lineno">Les <var>chestCount</var> lignes suivantes :</span> 4 entiers séparés par des espaces <var>chestId</var>, <var>cx</var>, <var>cy</var> et <var>chestGold</var>, l'identifiant, les coordonnées et la valeur en Or de chaque coffre.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Sortie pour un Tour de Jeu</div>
      <div class="text">
        <!-- BEGIN level1 level2 -->
        <p><span class="statement-lineno">Une ligne par navire que vous possédez</span> (dans l'ordre exact où ils apparaissent en entrée), contenant une commande de navigation :</p>
        <!-- END -->
        <!-- BEGIN level3 -->
        <p><span class="statement-lineno">Une ligne par navire que vous possédez</span> (dans l'ordre exact où ils apparaissent en entrée), contenant une commande de navigation éventuellement suivie d'un tir de canon :</p>
        <!-- END -->
        <ul>
          <li><action>SAIL</action> <var>x</var> <var>y</var> <var>power</var> pour pousser le navire vers les coordonnées entières (<var>x</var>, <var>y</var>) avec une puissance <var>power</var> (<const>0</const>&ndash;<const>100</const>).</li>
          <!-- BEGIN level2 level3 -->
          <li><action>ANCHOR</action> pour déployer le frein d'urgence.</li>
          <!-- END -->
        </ul>
        <!-- BEGIN level3 -->
        <p>Une commande <action>SAIL</action> ou <action>ANCHOR</action> peut être suivie d'un tir de canon (qui peut aussi être utilisé seul) :</p>
        <ul>
          <li><action>FIRE</action> <var>tx</var> <var>ty</var> pour tirer au canon sur les coordonnées entières (<var>tx</var>, <var>ty</var>).</li>
        </ul>
        <!-- END -->
        <!-- BEGIN level1 -->
        <p>Exemple : <action>SAIL 800 450 90</action></p>
        <!-- END -->
        <!-- BEGIN level2 -->
        <p>Exemples : <action>SAIL 800 450 90</action> ou <action>ANCHOR</action></p>
        <!-- END -->
        <!-- BEGIN level3 -->
        <p>Exemple : <action>SAIL 800 450 90 FIRE 750 400</action></p>
        <!-- END -->
        <p>Vous pouvez facultativement ajouter <action>MSG</action> <var>text</var> en fin de ligne pour afficher un message dans le viewer.</p>
      </div>
    </div>
    <div class="blk">
      <div class="title">Contraintes</div>
      <div class="text">
        <var>width</var> = <const>1600</const>, <var>height</var> = <const>900</const><br>
        <!-- BEGIN level1 -->
        <var>shipCount</var> = <const>2</const><br>
        <!-- END -->
        <!-- BEGIN level2 level3 -->
        <var>shipCount</var> = <const>4</const><br>
        <!-- END -->
        <const>0</const> &le; <var>chestCount</var> &le; <const>8</const><br>
        Temps de réponse pour le premier tour &le; <const>1000</const> ms<br>
        Temps de réponse par tour &le; <const>50</const> ms<br>
        Nombre maximum de tours : <const>120</const>
      </div>
    </div>
  </div>

  <!-- CODE SOURCE & CRÉDITS -->
  <div class="statement-section statement-examples">
    <h1><span class="icon icon-example">&nbsp;</span><span>Code Source &amp; Crédits</span></h1>
    <div class="statement-examples-text">
      <p>Le code source complet de l'arbitre (Referee), du viewer et des bots de référence est disponible sur GitHub :
        <a href="https://github.com/codingame-team/codingame-corsairs-maelstrom" target="_blank" rel="noopener noreferrer">https://github.com/codingame-team/codingame-corsairs-maelstrom</a>
      </p>
      <p><b>Crédits &amp; Inspiration :</b> Conception originale inspirée des jeux d'arène à physique inertielle et combats navals. Développé avec le SDK officiel CodinGame Game Engine.</p>
    </div>
  </div>
</div>
