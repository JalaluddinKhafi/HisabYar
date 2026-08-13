package com.hisabyar.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import java.util.List;

public class MainActivity extends Activity {
    private LinearLayout root, body;
    private TextView summary;
    private EditText amount, source, note;
    private Spinner type, currency, category, filterCurrency;
    private LedgerStore store;
    private String selectedDate=Dates.today();
    private Button dateButton, saveButton;
    private String editingId;
    private Entry lastDeleted;
    private Button undoButton;
    private static final String[] CATEGORIES={"غذا","کرایه خانه","ترانسپورت","برق و انترنت","صحت","آموزش","خانواده","خرید خانه","سایر"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        store=new LedgerStore(getSharedPreferences("hisabyar_local",MODE_PRIVATE));
        String problem=null;
        try { store.load(); } catch(Exception error) { problem="اطلاعات قبلی خوانده نشد. برای جلوگیری از پاک‌شدن اطلاعات، ثبت غیرفعال شده است."; }
        if(state!=null) selectedDate=state.getString("date",Dates.today());
        build(); render();
        if(problem!=null) message(problem);
    }
    @Override protected void onSaveInstanceState(Bundle state) {
        state.putString("date",selectedDate); super.onSaveInstanceState(state);
    }
    private void build() {
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); root.setPadding(dp(16),dp(16),dp(16),dp(16));
        TextView title=label(getString(R.string.app_name),28); title.setGravity(Gravity.CENTER); root.addView(title);
        root.addView(label(getString(R.string.tagline),14));
        summary=label("",16); root.addView(summary);
        root.addView(label("ثبت درآمد یا مصرف",20));
        type=spinner(new String[]{"مصرف","درآمد"}); root.addView(type);
        amount=edit("مبلغ"); amount.setTextDirection(View.TEXT_DIRECTION_LTR); amount.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); root.addView(amount);
        currency=spinner(new String[]{"افغانی (AFN)","دالر (USD)"}); root.addView(currency);
        category=spinner(CATEGORIES); root.addView(category);
        dateButton=button("تاریخ میلادی: "+selectedDate,()->chooseDate());
        source=edit("منبع درآمد / فروشگاه (اختیاری)"); root.addView(source);
        note=edit("توضیح (اختیاری)"); root.addView(note);
        saveButton=button(getString(R.string.save_entry),()->addEntry());
        button("پاک‌کردن فرم / لغو ویرایش",()->clearForm());
        button("گزارش امروز و این ماه",()->showReport());
        undoButton=button("بازگردانی آخرین حذف",()->undoDelete()); undoButton.setEnabled(false);
        root.addView(label("ثبت‌ها — لمس برای ویرایش؛ فشار طولانی برای حذف",18));
        filterCurrency=spinner(new String[]{"همهٔ ارزها","AFN","USD"}); root.addView(filterCurrency);
        watch(filterCurrency);
        body=new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL); root.addView(body);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.addView(root); setContentView(scroll);
    }
    private int dp(int value) { return Math.round(value*getResources().getDisplayMetrics().density); }
    private TextView label(String text,int size) {
        TextView view=new TextView(this); view.setText(text); view.setTextSize(size); view.setPadding(dp(8),dp(8),dp(8),dp(8)); return view;
    }
    private EditText edit(String hint) { EditText view=new EditText(this); view.setHint(hint); return view; }
    private Spinner spinner(String[] values) {
        Spinner view=new Spinner(this); view.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,values)); return view;
    }
    private void watch(Spinner view) {
        view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,View child,int position,long id) { if(body!=null) render(); }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private Button button(String text,Runnable action) {
        Button view=new Button(this); view.setText(text); view.setOnClickListener(v->action.run()); root.addView(view); return view;
    }
    private void message(String text) { new AlertDialog.Builder(this).setMessage(text).setPositiveButton("باشه",null).show(); }
    private void chooseDate() {
        String[] parts=selectedDate.split("-");
        new android.app.DatePickerDialog(this,(picker,year,month,day)->{
            selectedDate=String.format(java.util.Locale.US,"%04d-%02d-%02d",year,month+1,day);
            dateButton.setText("تاریخ میلادی: "+selectedDate);
        },Integer.parseInt(parts[0]),Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])).show();
    }
    private void addEntry() {
        try {
            try { Money.parse(amount.getText().toString()); }
            catch(IllegalArgumentException invalid) { amount.setError(invalid.getMessage()); amount.requestFocus(); return; }
            Entry entry=new Entry(editingId==null?Entry.newId():editingId,type.getSelectedItemPosition()==0?"expense":"income",
                amount.getText().toString(),currency.getSelectedItemPosition()==0?"AFN":"USD",
                category.getSelectedItem().toString(),source.getText().toString(),note.getText().toString(),selectedDate);
            store.upsert(entry); clearForm(); render();
            Toast.makeText(this,"ثبت شد",Toast.LENGTH_SHORT).show();
        } catch(Exception error) { message(error.getMessage()==null?"ثبت انجام نشد.":error.getMessage()); }
    }
    private void render() {
        String month=Dates.today().substring(0,7);
        summary.setText("این ماه ("+month+")\n"+totals(month));
        body.removeAllViews();
        for(Entry e:EntryFilter.select(store.entries(),filterCurrency.getSelectedItemPosition()==0?null:filterCurrency.getSelectedItem().toString(),null,null,null)) {
            TextView row=label((e.type.equals("income")?"درآمد":"مصرف")+" • "+Money.format(e.amount)+" "+e.currency+"\n"+e.date+" • "+e.category+"\n"+e.source+" "+e.note,16);
            row.setOnClickListener(v->editEntry(e));
            row.setOnLongClickListener(v->{delete(e);return true;}); body.addView(row);
        }
    }
    private void clearForm() {
        editingId=null; amount.setError(null); amount.setText(""); source.setText(""); note.setText("");
        selectedDate=Dates.today(); dateButton.setText("تاریخ میلادی: "+selectedDate);
        saveButton.setText(getString(R.string.save_entry));
    }
    private void editEntry(Entry e) {
        editingId=e.id; amount.setText(e.amount.toPlainString()); source.setText(e.source); note.setText(e.note);
        type.setSelection(e.type.equals("income")?1:0); currency.setSelection(e.currency.equals("USD")?1:0);
        for(int i=0;i<category.getCount();i++) if(e.category.equals(category.getItemAtPosition(i))) category.setSelection(i);
        selectedDate=e.date; dateButton.setText("تاریخ میلادی: "+selectedDate); saveButton.setText("ذخیرهٔ ویرایش");
        amount.requestFocus();
    }
    private void delete(Entry entry) {
        new AlertDialog.Builder(this).setTitle("حذف ثبت؟").setMessage(entry.date+" • "+entry.category+"\n"+Money.format(entry.amount)+" "+entry.currency+"\nاین ثبت حذف شود؟")
            .setNegativeButton("لغو",null).setPositiveButton("حذف",(dialog,which)->{
                try { lastDeleted=store.remove(entry.id); undoButton.setEnabled(true); if(entry.id.equals(editingId)) clearForm(); render(); } catch(Exception error) { message(error.getMessage()); }
            }).show();
    }
    private void undoDelete() {
        if(lastDeleted==null) return;
        try { store.upsert(lastDeleted); lastDeleted=null; undoButton.setEnabled(false); render(); }
        catch(Exception error) { message(error.getMessage()); }
    }
    private String totals(String prefix) {
        StringBuilder text=new StringBuilder();
        for(String cur:new String[]{"AFN","USD"}) text.append(cur).append("\nدرآمد: ")
            .append(Money.format(Ledger.sum(store.entries(),"income",cur,prefix))).append("\nمصرف: ")
            .append(Money.format(Ledger.sum(store.entries(),"expense",cur,prefix))).append("\nخالص: ")
            .append(Money.format(Ledger.balance(store.entries(),cur,prefix))).append("\n");
        return text.toString();
    }
    private void showReport() {
        String today=Dates.today(); message("گزارش امروز ("+today+")\n"+totals(today)+"\nاین ماه\n"+totals(today.substring(0,7)));
    }
}
