package com.busanit.anifamily.protect

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.District
import com.busanit.anifamily.R
import com.busanit.anifamily.databinding.QueryDialogueBinding
import com.busanit.anifamily.protect.model.QueryVO
import java.time.LocalDate

class QueryDialogue(private val context: AppCompatActivity) {

    private lateinit var binding: QueryDialogueBinding
    private lateinit var listener: QueryDialogueQueryClickedListener
    private val dlg = Dialog(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun show(queryVO: QueryVO) {

        var today: LocalDate = LocalDate.now()
        var fromday: LocalDate = today.minusMonths(3)
        val allDistrict = District.readDistrict(context)
        val cities = context.resources!!.getStringArray(R.array.city)
        val kindArray = arrayOf("전체", "개", "고양이", "기타")

        val idx = cities.indexOf(queryVO.city)

        val cityAdapter =
            ArrayAdapter(context, R.layout.spinner_list, cities)
        cityAdapter.setDropDownViewResource(R.layout.spinner_list)

        var districts: Array<String> = arrayOf("전체")
        var districtAdapter: ArrayAdapter<String>? = null

        if (queryVO.city != "모든지역") {
            for (district in allDistrict!!) {
                if (district.toList()[4].contains(queryVO.city)) {
                    districts += district.toList()[3]
                }
            }
            districtAdapter = ArrayAdapter(context, R.layout.spinner_list, districts)
            districtAdapter.setDropDownViewResource(R.layout.spinner_list)
        }

        val kindAdapter =
            ArrayAdapter(context, R.layout.spinner_list, kindArray)
        kindAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding = QueryDialogueBinding.inflate(context.layoutInflater)

        if (queryVO.recent) binding.recent.isChecked = true

        if (binding.toDay.text == "") binding.toDay.text = today.toString()
        if (binding.fromDay.text == "") binding.fromDay.text = fromday.toString()

        binding.city.adapter = cityAdapter
        binding.city.setSelection(idx)

        val idxKind = kindArray.indexOf(queryVO.kind)
        binding.kind.adapter = kindAdapter
        binding.kind.setSelection(idxKind)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(binding.root)
        dlg.setCancelable(false)

        binding.toDay.setOnClickListener {
            DatePickerDialog(context,
                { _, year, month, dayOfMonth ->
                    today = LocalDate.parse("$year-${
                        String.format("%02d",
                            month + 1)
                    }-${String.format("%02d", dayOfMonth)}")
                    binding.toDay.text = today.toString()
                }, today.year, today.monthValue - 1, today.dayOfMonth).show()
        }

        binding.fromDay.setOnClickListener {
            DatePickerDialog(context,
                { _, year, month, dayOfMonth ->
                    fromday = LocalDate.parse("$year-${
                        String.format("%02d",
                            month + 1)
                    }-${String.format("%02d", dayOfMonth)}")
                    binding.fromDay.text = fromday.toString()
                }, fromday.year, fromday.monthValue - 1, fromday.dayOfMonth).show()
        }

        binding.city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position != 0) {
                    districts = arrayOf("전체")

                    for (district in allDistrict!!) {
                        if (district.toList()[4].contains(binding.city.selectedItem.toString())) {
                            districts += district.toList()[3]
                        }
                    }
                    districtAdapter = ArrayAdapter(context, R.layout.spinner_list, districts)
                    districtAdapter!!.setDropDownViewResource(R.layout.spinner_list)

                    val idxDistrict = districts.indexOf(queryVO.district)
                    binding.district.adapter = districtAdapter
                    binding.district.setSelection(idxDistrict)

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.query.setOnClickListener {
            listener.onQueryClicked(queryVO)
            dlg.dismiss()
        }

        dlg.show()
    }

    fun setQueryClickedListener(listener: (QueryVO) -> Unit) {
        this.listener = object : QueryDialogueQueryClickedListener {
            override fun onQueryClicked(queryVO: QueryVO) {

                queryVO.fromDay = binding.fromDay.text.toString()
                queryVO.toDay = binding.toDay.text.toString()
                queryVO.recent = binding.recent.isChecked
                queryVO.city = binding.city.selectedItem.toString()
                queryVO.district = binding.district.selectedItem.toString()

                queryVO.kind = binding.kind.selectedItem.toString()
                queryVO.protecting = binding.protecting.isChecked
                queryVO.alertResque = binding.alertRescue.isChecked

                listener(queryVO)
            }
        }
    }

    interface QueryDialogueQueryClickedListener {
        fun onQueryClicked(queryVO: QueryVO)
    }
}


