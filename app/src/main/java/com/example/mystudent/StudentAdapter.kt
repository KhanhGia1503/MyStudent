package com.example.mystudent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(val students: MutableList<StudentModel>, private val recyclerView: RecyclerView): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  private var recentlyDeletedStudent: StudentModel? = null
  private var recentlyDeletedStudentPosition: Int = -1

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
      R.layout.layout_student_item,
      parent, false
    )
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    holder.imageEdit.setOnClickListener {
      val dialogView =
        LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_add_student, null)
      val studentNameInput = dialogView.findViewById<EditText>(R.id.edit_student_name)
      val studentIdInput = dialogView.findViewById<EditText>(R.id.edit_student_id)

      studentNameInput.setText(student.studentName)
      studentIdInput.setText(student.studentId)

      AlertDialog.Builder(holder.itemView.context)
        .setTitle("Cập nhật sinh viên")
        .setView(dialogView)
        .setPositiveButton("Cập nhật") { _, _ ->
          val updatedName = studentNameInput.text.toString()
          val updatedId = studentIdInput.text.toString()
          if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
            students[position] = StudentModel(updatedName, updatedId)
            notifyDataSetChanged()
          }
        }
        .setNegativeButton("Hủy", null)
        .show()
    }

    holder.imageRemove.setOnClickListener {
      recentlyDeletedStudent = students[position]
      recentlyDeletedStudentPosition = position

      AlertDialog.Builder(holder.itemView.context)
        .setTitle("Xóa sinh viên")
        .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
        .setPositiveButton("Chắc chắn") { _, _ ->
          students.removeAt(position)
          notifyDataSetChanged()

          Snackbar.make(recyclerView, "Sinh viên đã bị xóa", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
              recentlyDeletedStudent?.let {
                students.add(recentlyDeletedStudentPosition, it)
                notifyDataSetChanged()
              }
            }
            .show()
        }
        .setNegativeButton("Không", null)
        .show()
    }

  }
}