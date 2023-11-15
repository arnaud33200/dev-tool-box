import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MovingHorizontalDotAnimation(
    modifier: Modifier = Modifier,
    color: Color,
    animationDurationMillis: Int = 800,
    circleSize: Dp = 8.dp,
    circleCount: Int = 3,
) {
    require(circleCount >= 2)

    val circleRadius = with(LocalDensity.current) {
        (circleSize / 2).toPx()
    }
    val circleDiameter = circleRadius * 2
    val separation = with(LocalDensity.current) {
        (circleSize / 2).toPx()
    }

    val animation = rememberInfiniteTransition()
    val animationSpec = InfiniteRepeatableSpec<Float>(
        animation = tween(durationMillis = animationDurationMillis),
        repeatMode = RepeatMode.Restart,
    )

    val enteringRadius by animation.animateFloat(
        initialValue = 0f,
        targetValue = circleRadius,
        animationSpec = animationSpec
    )
    val exitingRadius by animation.animateFloat(
        initialValue = circleRadius,
        targetValue = 0f,
        animationSpec = animationSpec
    )
    val width = circleSize * (circleCount + (circleCount - 1) / 2f)

    Canvas(
        modifier = modifier
            .width(width)
            .height(circleSize),
        onDraw = {
            val adjustedSeparation = (enteringRadius / circleRadius) * separation
            val enteringDiameter = 2 * enteringRadius
            val radiusOffset = Offset(circleRadius, circleRadius)

            drawCircle(
                color = color,
                radius = enteringRadius,
                center = Offset.Zero + radiusOffset
            )

            repeat(circleCount - 1) {
                drawCircle(
                    color = color,
                    radius = circleRadius,
                    center = Offset(it * circleDiameter + it * separation, 0f) +
                        Offset(enteringDiameter + adjustedSeparation, 0f) +
                        radiusOffset
                )
            }
            drawCircle(
                color = color,
                radius = exitingRadius,
                center = Offset(
                    (circleCount - 1) * circleDiameter + (circleCount - 1) * separation,
                    0f
                ) +
                    Offset(enteringDiameter - adjustedSeparation, 0f) +
                    radiusOffset
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun MovingHorizontalDotAnimationPreview() {
    Surface {
        MovingHorizontalDotAnimation()
    }
}
