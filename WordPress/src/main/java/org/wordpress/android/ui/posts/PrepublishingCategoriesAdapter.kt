package org.wordpress.android.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.prepublishing_categories_row.view.*
import org.apache.commons.text.StringEscapeUtils
import org.wordpress.android.R
import org.wordpress.android.models.CategoryNode
import org.wordpress.android.ui.posts.PrepublishingCategoriesAdapter.CategoriesViewHolder

class PrepublishingCategoriesAdapter(
    private val context: Context,
    private val onCheckChangeListener: ((Long, Boolean) -> Unit)
) : RecyclerView.Adapter<CategoriesViewHolder>() {
    var categoryNodeList: List<CategoryNode> = ArrayList()
        set(value) {
            if (!isSameCategoryList(value)) {
                field = value
                notifyDataSetChanged()
            }
        }

    // todo: annmarie - may not need this at all
    private var selectedCategoryIds: HashSet<Long> = hashSetOf()

    interface OnCategoryClickedListener {
        fun onCategorySelected(categoryId: Long)
        fun onCategoryUnselected(categoryId: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.prepublishing_categories_row, parent, false)
        return CategoriesViewHolder(onCheckChangeListener, itemView)
    }

    override fun onBindViewHolder(viewHolder: CategoriesViewHolder, position: Int) {
       viewHolder.bind(categoryNodeList[position])
    }

    init {
        setHasStableIds(true)
    }

    fun setItemChecked(categoryId: Long) {
        // todo: annmarie implement this
    }

    override fun getItemId(position: Int): Long {
        return categoryNodeList[position].categoryId
    }

    override fun getItemCount(): Int {
        return categoryNodeList.size
    }

    private fun isSameCategoryList(categoryList: List<CategoryNode>): Boolean {
        if (categoryList.size != categoryNodeList.size) {
            return false
        }

        categoryNodeList.forEach {
            if (!containsNode(it)) {
                return false
            }
        }
        return true
    }

    private fun containsNode(categoryNode: CategoryNode): Boolean {
        categoryNodeList.forEach {
            if (it.categoryId == categoryNode.categoryId) {
                return true
            }
        }
        return false
    }

    class CategoriesViewHolder(private val onCheckedChangeClickListener: ((Long, Boolean) -> Unit),
        itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(row: CategoryNode) = with(itemView) {

            val verticalPadding: Int = prepublishing_category_text.resources.getDimensionPixelOffset(R.dimen.margin_large)
            val horizontalPadding: Int = prepublishing_category_text.resources.getDimensionPixelOffset(R.dimen.margin_extra_large)

            ViewCompat.setPaddingRelative(
                    prepublishing_category_text,
                    horizontalPadding * row.level,
                    verticalPadding,
                    horizontalPadding,
                    verticalPadding
            )

            prepublishing_category_text.text = StringEscapeUtils.unescapeHtml4(
                    row.name
            )

            prepublishing_category_check.isChecked = true
            prepublishing_category_check.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckedChangeClickListener.invoke(row.categoryId, isChecked)
            }
        }
    }
}