# Corsairs of the Maelstrom — CodinGame Multiplayer SDK Project

Un puzzle multijoueur algorithmique basé sur la **cinématique inertielle à masse variable**, la **navigation dans un champ de vecteurs tourbillonnant (Maelström)** et la **balistique prédictive**, développé avec le **CodinGame Game Engine (`4.7.7`)** en **3 ligues progressives** (**Bois 2 → Bois 1 → Bronze+**).

---

## 1. Commandes Rapides

### Lancer le Viewer Web Local (`http://localhost:8888/test.html`)
```bash
cd corsairs-maelstrom-sdk
mvn test-compile exec:java -Dexec.mainClass="Main" -Dexec.classpathScope="test"
```

### Lancer le Banc de Simulation Rapide (Sans Serveur Web)
```bash
cd corsairs-maelstrom-sdk
mvn test-compile exec:java -Dexec.mainClass="Main" -Dexec.classpathScope="test" -Dexec.args="--simulate"
```
