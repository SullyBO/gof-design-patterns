/**
 * Bridge pattern composition demonstration
 *
 * Honestly, this is so simple and easy to use that it's hard to get
 * too creative here. maybe if the constructor API was more complicated
 * I could've shown how an elegant progressive API usecase would look
 * like but then the bridge implementation would've gotten too in the
 * weeds.
 *
 * For what its worth, this does demonstrate how much complexity we
 * can hide while enabling runtime configuration.
 */
fun main() {
    val pixelPlayer = Player(PixelRenderer())
    val voxelEnemy = Enemy(VoxelRenderer())

    pixelPlayer.draw()
    voxelEnemy.draw()
}
