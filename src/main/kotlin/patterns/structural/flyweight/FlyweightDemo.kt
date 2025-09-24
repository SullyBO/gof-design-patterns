/**
 * The real neat thing here to demonstrate is the transparent
 * memory management system. 
 * 
 * Notice how the second time release
 * texture is called, the printed message is different to denote
 * that the file is no longer in memory (watch for "Freed texture"
 * vs no message.) All while the user is using a very simple API.
 * 
 */
fun main() {
    val sprite1 = GameSprite(AssetManager.getTexture("tree.png"))
    val sprite2 = GameSprite(AssetManager.getTexture("tree.png"))
    
    sprite1.moveTo(10f, 20f)
    sprite2.moveTo(50f, 60f)
    
    sprite1.render()
    sprite2.render()
    
    AssetManager.releaseTexture("tree.png")
    AssetManager.releaseTexture("tree.png")
}