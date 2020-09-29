package de.rohnert.smarteatingsystem.frontend.foodtracker.adapter

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R

class MealListRecyclerViewAdapter():RecyclerView.Adapter<MealListRecyclerViewAdapter.ViewHolder>() {







    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var mainContent:ConstraintLayout = itemView.findViewById(R.id.item_main_content)
        var subContent:ConstraintLayout = itemView.findViewById(R.id.item_sub_content)
        var btnDropDown:ImageButton = itemView.findViewById(R.id.item_btn_dropdown)

        init {
            subContent.visibility = View.GONE
            subContent.alpha = 0f

            btnDropDown.setOnClickListener {
                if(subContent.visibility == View.GONE)
                    showContent()
                else
                    hideContent()
            }

            mainContent.setOnClickListener {
                Toast.makeText(itemView.context,"Mahlzeit wird eingetragen...",Toast.LENGTH_SHORT).show()
            }
        }

        private fun showContent()
        {

            btnDropDown.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            subContent.visibility = View.VISIBLE

            subContent.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(object :Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        subContent.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })

            /*subContent.animate()
                .alpha(1.0f)
                .scaleY(1f)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        subContent.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {
                        subContent.visibility = View.VISIBLE
                    }

                })
                .setDuration(500)
                .start()*/
        }

        private fun hideContent()
        {


            btnDropDown.setImageResource(R.drawable.ic_arrow_down)
            subContent.animate()
                .translationY(-subContent.height.toFloat()/4)
                .alpha(0f)
                .setDuration(500)
                .setListener(object :Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        subContent.visibility = View.GONE
                        subContent.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })



            /*subContent.animate()
                .alpha(0.0f)
                .scaleY(0f)
                .setListener(object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        subContent.visibility = View.GONE
                        subContent.clearAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                })
                .setDuration(500)
                .start()*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_meallist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}