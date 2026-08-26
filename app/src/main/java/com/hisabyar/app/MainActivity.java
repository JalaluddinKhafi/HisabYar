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
    private EditText amount, source, note, search;
    private Spinner type, currency, category, filterCurrency, filterType, filterPeriod;
    private LedgerStore store;
    private String selectedDate=Dates.today();
    private Button dateButton, saveButton;
    private String editingId;
    private Entry lastDeleted;
    private Button undoButton;
    private int visibleLimit=50;
    private static final String[] CATEGORIES={"غذا","کرایه خانه","ترانسپورت","برق و انترنت","صحت","آموزش","خانواده","خرید خانه","معاش","فروش","هدیه","کار آزاد","سایر"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        store=new LedgerStore(getSharedPreferences("hisabyar_local",MODE_PRIVATE));
        String problem=null;
        try { store.load(); } catch(Exception error) { problem="اطلاعات قبلی خوانده نشد. برای جلوگیری از پاک‌شدن اطلاعات، ثبت غیرفعال شده است."; }
        if(state!=null) selectedDate=state.getString("date",Dates.today());
        build();
        if(state!=null) {
            filterCurrency.setSelection(state.getInt("filterCurrency",0)); filterType.setSelection(state.getInt("filterType",0));
            filterPeriod.setSelection(state.getInt("filterPeriod",0)); search.setText(state.getString("search",""));
        }
        if(state!=null) {
            amount.setText(state.getString("amount","")); source.setText(state.getString("source","")); note.setText(state.getString("note",""));
            type.setSelection(state.getInt("type",0)); currency.setSelection(state.getInt("currency",0)); selectCategory(state.getString("categoryName",CATEGORIES[0]));
            editingId=state.getString("editingId"); if(editingId!=null) saveButton.setText("ذخیرهٔ ویرایش");
        }
        if(state!=null && state.containsKey("lastDeleted")) {
            try { lastDeleted=Entry.fromJson(new org.json.JSONObject(state.getString("lastDeleted"))); undoButton.setEnabled(true); }
            catch(Exception ignored) { lastDeleted=null; }
        }
        render();
        if(problem!=null) message(problem);
    }
    @Override protected void onResume() { super.onResume(); if(body!=null) render(); }
    @Override protected void onSaveInstanceState(Bundle state) {
        state.putString("amount",amount.getText().toString()); state.putString("source",source.getText().toString()); state.putString("note",note.getText().toString());
        state.putInt("type",type.getSelectedItemPosition()); state.putInt("currency",currency.getSelectedItemPosition()); state.putString("categoryName",category.getSelectedItem().toString());
        state.putString("editingId",editingId);
        if(lastDeleted!=null) try { state.putString("lastDeleted",lastDeleted.toJson().toString()); } catch(org.json.JSONException ignored) {}
        state.putInt("filterCurrency",filterCurrency.getSelectedItemPosition()); state.putInt("filterType",filterType.getSelectedItemPosition());
        state.putInt("filterPeriod",filterPeriod.getSelectedItemPosition()); state.putString("search",search.getText().toString());
        state.putString("date",selectedDate); super.onSaveInstanceState(state);
    }
    private void build() {
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); root.setPadding(dp(16),dp(16),dp(16),dp(16));
        TextView title=label(getString(R.string.app_name),28); title.setGravity(Gravity.CENTER); root.addView(title);
        root.addView(label(getString(R.string.tagline),14));
        summary=label("",16); root.addView(summary);
        root.addView(label("ثبت درآمد یا مصرف",20));
        type=spinner(new String[]{"مصرف","درآمد"}); root.addView(type);
        amount=edit("مبلغ"); amount.setId(R.id.entry_amount); amount.setTextDirection(View.TEXT_DIRECTION_LTR); amount.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); root.addView(amount);
        currency=spinner(new String[]{"افغانی (AFN)","دالر (USD)"}); root.addView(currency);
        java.util.LinkedHashSet<String> categories=new java.util.LinkedHashSet<>(java.util.Arrays.asList(CATEGORIES));
        for(Entry entry:store.entries()) categories.add(entry.category);
        category=spinner(categories.toArray(new String[0])); root.addView(category);
        type.setContentDescription("نوع ثبت"); currency.setContentDescription("ارز ثبت"); category.setContentDescription("دسته‌بندی ثبت");
        dateButton=button("تاریخ میلادی: "+selectedDate,()->chooseDate());
        source=edit("منبع درآمد / فروشگاه (اختیاری)"); source.setId(R.id.entry_source); root.addView(source);
        note=edit("توضیح (اختیاری)"); note.setId(R.id.entry_note); root.addView(note);
        amount.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(24)});
        source.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(200)});
        note.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(1000)});
        saveButton=button(getString(R.string.save_entry),()->addEntry());
        button("پاک‌کردن فرم / لغو ویرایش",()->confirmDiscard(()->clearForm()));
        button("گزارش امروز و این ماه",()->showReport());
        button("خروجی CSV همهٔ ثبت‌ها",()->createDocument(43,"text/csv","HisabYar-"+Dates.today()+".csv"));
        button("بازیابی پشتیبان JSON",()->openBackup());
        button("خروجی خام برای بازیابی اطلاعات خراب",()->createDocument(42,"application/json","HisabYar-raw-"+Dates.today()+".json"));
        button("ذخیرهٔ فایل پشتیبان JSON",()->createDocument(40,"application/json","HisabYar-"+Dates.today()+".json"));
        button("مصارف این ماه بر اساس دسته",()->message(CategoryReport.expenses(store.entries(),Dates.today().substring(0,7))));
        undoButton=button("بازگردانی آخرین حذف",()->undoDelete()); undoButton.setEnabled(false);
        root.addView(label("ثبت‌ها — لمس برای ویرایش؛ فشار طولانی برای حذف",18));
        filterCurrency=spinner(new String[]{"همهٔ ارزها","AFN","USD"}); root.addView(filterCurrency);
        watch(filterCurrency);
        filterType=spinner(new String[]{"همهٔ ثبت‌ها","مصرف","درآمد"}); root.addView(filterType); watch(filterType);
        filterPeriod=spinner(new String[]{"همهٔ تاریخ‌ها","امروز","این ماه"}); root.addView(filterPeriod); watch(filterPeriod);
        search=edit("جستجو در توضیح، منبع، دسته، مبلغ یا تاریخ"); search.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(200)}); root.addView(search);
        search.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
            public void onTextChanged(CharSequence s,int start,int before,int count) { if(body!=null) { visibleLimit=50; render(); } }
            public void afterTextChanged(android.text.Editable s) {}
        });
        filterCurrency.setContentDescription("فیلتر ارز"); filterType.setContentDescription("فیلتر نوع ثبت"); filterPeriod.setContentDescription("فیلتر بازهٔ زمانی");
        body=new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL); root.addView(body);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.addView(root);
        scroll.setOnApplyWindowInsetsListener((view,insets)->{
            if(android.os.Build.VERSION.SDK_INT>=30) {
                android.graphics.Insets bars=insets.getInsets(android.view.WindowInsets.Type.systemBars()|android.view.WindowInsets.Type.ime());
                view.setPadding(bars.left,bars.top,bars.right,bars.bottom);
            } else view.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());
            return insets;
        });
        setContentView(scroll);
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
            public void onItemSelected(AdapterView<?> parent,View child,int position,long id) { if(body!=null) { visibleLimit=50; render(); } }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private Button button(String text,Runnable action) {
        Button view=new Button(this); view.setText(text); view.setMinHeight(dp(48)); view.setOnClickListener(v->action.run()); root.addView(view); return view;
    }
    private void message(String text) {
        ScrollView scroll=new ScrollView(this); TextView content=label(text==null?"عملیات انجام نشد.":text,16);
        content.setTextIsSelectable(true); scroll.addView(content);
        new AlertDialog.Builder(this).setView(scroll).setPositiveButton(getString(R.string.ok),null).show();
    }
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
    private String periodPrefix() {
        int position=filterPeriod.getSelectedItemPosition();
        return position==0?null:position==1?Dates.today():Dates.today().substring(0,7);
    }
    private void render() {
        String month=Dates.today().substring(0,7);
        summary.setText("خلاصهٔ همهٔ ثبت‌های این ماه ("+month+")\n"+totals(month));
        boolean deficit=Ledger.balance(store.entries(),"AFN",month).signum()<0 || Ledger.balance(store.entries(),"USD",month).signum()<0;
        summary.setTextColor(deficit?Color.rgb(160,35,35):Color.rgb(20,90,50));
        body.removeAllViews();
        List<Entry> filtered=EntryFilter.select(store.entries(),filterCurrency.getSelectedItemPosition()==0?null:filterCurrency.getSelectedItem().toString(),filterType.getSelectedItemPosition()==0?null:filterType.getSelectedItemPosition()==1?"expense":"income",periodPrefix(),search.getText().toString());
        if(filtered.isEmpty()) body.addView(label(store.isReadOnly()?"اطلاعات خوانده نشد؛ نسخهٔ خام را ذخیره کنید.":store.entries().isEmpty()?"هنوز ثبتی ندارید. اولین درآمد یا مصرف را وارد کنید.":"هیچ ثبتی با این فیلترها پیدا نشد.",16));
        else body.addView(label("تعداد نتایج: "+filtered.size(),14));
        int shown=0;
        for(Entry e:filtered) {
            if(shown++>=visibleLimit) break;
            TextView row=label((e.type.equals("income")?"درآمد":"مصرف")+" • "+Money.format(e.amount)+" "+e.currency+"\n"+e.date+" • "+e.category+"\n"+e.source+" "+e.note,16);
            row.setFocusable(true); row.setContentDescription(row.getText()+"؛ لمس برای ویرایش، فشار طولانی برای حذف");
            row.setOnClickListener(v->confirmDiscard(()->editEntry(e)));
            row.setOnLongClickListener(v->{delete(e);return true;}); body.addView(row);
        }
        if(filtered.size()>visibleLimit) {
            Button more=new Button(this); more.setText("نمایش ۵۰ ثبت دیگر");
            more.setOnClickListener(v->{visibleLimit+=50; render();}); body.addView(more);
        }
    }
    private boolean hasDraft() {
        return editingId!=null || !amount.getText().toString().isEmpty() || !source.getText().toString().isEmpty() || !note.getText().toString().isEmpty();
    }
    private void confirmDiscard(Runnable action) {
        if(!hasDraft()) { action.run(); return; }
        new AlertDialog.Builder(this).setMessage("تغییرات ذخیره‌نشدهٔ فرم کنار گذاشته شوند؟")
            .setNegativeButton(getString(R.string.cancel),null).setPositiveButton("کنارگذاشتن",(dialog,which)->action.run()).show();
    }
    @Override public void onBackPressed() { confirmDiscard(()->finish()); }
    private void clearForm() {
        editingId=null; amount.setError(null); amount.setText(""); source.setText(""); note.setText("");
        selectedDate=Dates.today(); dateButton.setText("تاریخ میلادی: "+selectedDate);
        saveButton.setText(getString(R.string.save_entry));
    }
    private void editEntry(Entry e) {
        editingId=e.id; amount.setText(e.amount.toPlainString()); source.setText(e.source); note.setText(e.note);
        type.setSelection(e.type.equals("income")?1:0); currency.setSelection(e.currency.equals("USD")?1:0);
        selectCategory(e.category);
        selectedDate=e.date; dateButton.setText("تاریخ میلادی: "+selectedDate); saveButton.setText("ذخیرهٔ ویرایش");
        amount.requestFocus();
    }
    private void selectCategory(String selected) {
        boolean foundCategory=false;
        for(int i=0;i<category.getCount();i++) if(selected.equals(category.getItemAtPosition(i))) { category.setSelection(i); foundCategory=true; }
        if(!foundCategory) {
            java.util.ArrayList<String> values=new java.util.ArrayList<>(); for(int i=0;i<category.getCount();i++) values.add(category.getItemAtPosition(i).toString()); values.add(selected);
            category.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,values)); category.setSelection(values.size()-1);
        }
    }
    private void delete(Entry entry) {
        new AlertDialog.Builder(this).setTitle("حذف ثبت؟").setMessage(entry.date+" • "+entry.category+"\n"+Money.format(entry.amount)+" "+entry.currency+"\nاین ثبت حذف شود؟")
            .setNegativeButton(getString(R.string.cancel),null).setPositiveButton("حذف",(dialog,which)->{
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
    private void openBackup() {
        android.content.Intent intent=new android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(android.content.Intent.CATEGORY_OPENABLE); intent.setType("*/*");
        try { startActivityForResult(intent,41); } catch(android.content.ActivityNotFoundException error) { message("انتخابگر فایل روی دستگاه موجود نیست."); }
    }
    private void previewImport(List<Entry> imported) {
        if(store.isReadOnly()) { message("برای محافظت از دادهٔ خراب، بازیابی غیرفعال است. ابتدا نسخهٔ خام را خروجی بگیرید."); return; }
        new AlertDialog.Builder(this).setTitle("جایگزینی تمام اطلاعات؟")
            .setMessage("پشتیبان معتبر است: "+imported.size()+" ثبت.\nتمام "+store.entries().size()+" ثبت فعلی جایگزین می‌شوند. ابتدا از اطلاعات فعلی پشتیبان بگیرید.")
            .setNegativeButton(getString(R.string.cancel),null).setPositiveButton("جایگزینی",(dialog,which)->{
                try { store.replace(imported); lastDeleted=null; undoButton.setEnabled(false); clearForm(); render(); message("بازیابی انجام شد."); }
                catch(Exception error) { message("بازیابی انجام نشد: "+error.getMessage()); }
            }).show();
    }
    private void createDocument(int request,String mime,String filename) {
        if((request==40 || request==43) && store.isReadOnly()) { message("اطلاعات خوانده نشد؛ از خروجی خام استفاده کنید."); return; }
        android.content.Intent intent=new android.content.Intent(android.content.Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(android.content.Intent.CATEGORY_OPENABLE); intent.setType(mime);
        intent.putExtra(android.content.Intent.EXTRA_TITLE,filename);
        try { startActivityForResult(intent,request); } catch(android.content.ActivityNotFoundException error) { message("انتخابگر فایل روی دستگاه موجود نیست."); }
    }
    private void background(Runnable work) { new Thread(work,"HisabYar-files").start(); }
    private void onUi(Runnable work) { runOnUiThread(()->{if(!isFinishing() && !isDestroyed()) work.run();}); }
    @Override protected void onActivityResult(int request,int result,android.content.Intent data) {
        super.onActivityResult(request,result,data);
        if(result!=RESULT_OK || data==null || data.getData()==null) return;
        android.net.Uri uri=data.getData();
        if(request==41) {
            background(()->{
                try { List<Entry> imported=BackupCodec.decode(DocumentFiles.read(getContentResolver().openInputStream(uri))); onUi(()->previewImport(imported)); }
                catch(Exception error) { onUi(()->message("پشتیبان نامعتبر یا خواندن فایل ناممکن است. اطلاعات فعلی تغییر نکرد.")); }
            });
        }
        if((request==40 || request==43) && store.isReadOnly()) { message("اطلاعات خوانده نشد؛ از گزینهٔ خروجی خام استفاده کنید."); return; }
        if(request==40 || request==42 || request==43) {
            final String text;
            try { text=request==42?store.raw():request==43?CsvExport.encode(store.entries()):BackupCodec.encode(store.entries()); } catch(Exception error) { message("تهیهٔ پشتیبان انجام نشد."); return; }
            background(()->{
                try { DocumentFiles.write(getContentResolver().openOutputStream(uri,"wt"),text); onUi(()->message("فایل ذخیره شد. آن را در جای امن نگه دارید.")); }
                catch(Exception error) { onUi(()->message("نوشتن فایل انجام نشد؛ فایل مقصد را بررسی کنید.")); }
            });
        }
    }
    private void showReport() {
        String today=Dates.today(); message("گزارش امروز ("+today+")\n"+totals(today)+"\nاین ماه\n"+totals(today.substring(0,7)));
    }
}
