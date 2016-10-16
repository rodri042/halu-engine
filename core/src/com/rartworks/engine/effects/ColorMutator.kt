package com.rartworks.engine.effects

import com.badlogic.gdx.graphics.Color
import com.rartworks.engine.rendering.Drawable
import com.rartworks.engine.utils.getRandomElement
import com.rartworks.engine.utils.isEqualWithDelta

/**
 * Changes randomly the color of the [container].
 * Also it can do a transition to a fixed color.
 */
class ColorMutator(container: Drawable, private val availableColors: List<Color>) {
	private val INTERPOLATION_COEFFICIENT = 0.5f
	private val COMPARISON_DELTA = 0.1f

	private val color = container.color
	private val isTheRightColor: Boolean get() =
	isTheRightComponent({ it.r }) && isTheRightComponent({ it.g }) &&
		isTheRightComponent({ it.b }) && isTheRightComponent({ it.a })

	private lateinit var nextColor: Color
	private var shouldContinue = true


	init {
		this.selectNextColor()
	}

	/**
	 * Slightly changes the container's color to the [nextColor].
	 */
	fun mutate(delta: Float) {
		this.color.lerp(this.nextColor, INTERPOLATION_COEFFICIENT * delta)

		if (this.isTheRightColor && this.shouldContinue)
			this.selectNextColor()
	}

	/**
	 * Resets the color to WHITE.
	 */
	fun reset() {
		this.color.set(Color.WHITE)
		this.shouldContinue = true
	}

	/**
	 * Selects [color] to be the next color and pauses all the future transitions.
	 */
	fun setFixedColor(color: Color) {
		this.nextColor = color
		this.shouldContinue = false
	}

	/**
	 * Keep displaying random colors.
	 */
	fun unsetFixedColor()  {
		this.shouldContinue = true
	}

	/**
	 * Selects a random color to be displayed next.
	 */
	private fun selectNextColor() {
		this.nextColor = this.availableColors.getRandomElement()
	}

	/**
	 * Determines if a [component] of the [Color] is OK.
	 */
	private inline fun isTheRightComponent(component: (Color) -> (Float)): Boolean {
		return component(this.color).isEqualWithDelta(component(this.nextColor), COMPARISON_DELTA)
	}
}
