package com.hisabyar.app;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.content.*;
import org.json.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity {
  LinearLayout root, body; TextView summary; EditText amount, note, source; Spinner currency, category, type; SharedPreferences prefs;
  JSONArray entries = new JSONArray();
  String[] currencies={"AFN - افغانی","USD - دالر"};
  String[] categories={"غذا","کرایه خانه","ترانسپورت","برق و انترنت","صحت","آموزش","خانواده","خرید خانه","سایر"};
  @Override public void onCreate(Bundle b){super.onCreate(b); prefs=getSharedPreferences("hisabyar_local",MODE_PRIVATE); load(); build(); render();}
  void build(){
    root=new LinearLayout(this); root.setOrientation(1); root.setPadding(18,18,18,18); root.setBackgroundColor(Color.rgb(246,248,251));
    TextView title=new TextView(this); title.setText("حساب‌یار"); title.setTextSize(28); title.setTextColor(Color.rgb(26,47,78)); title.setGravity(Gravity.CENTER); root.addView(title,new LinearLayout.LayoutParams(-1,58));
    TextView privacy=new TextView(this); privacy.setText("دفتر درآمد و مصارف شخصی • آفلاین"); privacy.setTextColor(Color.DKGRAY); privacy.setGravity(Gravity.CENTER); root.addView(privacy);
    summary=new TextView(this); summary.setTextSize(16); summary.setTextColor(Color.rgb(30,55,70)); summary.setPadding(14,14,14,14); summary.setBackgroundColor(Color.WHITE); root.addView(summary,new LinearLayout.LayoutParams(-1,-2));
    TextView formTitle=new TextView(this); formTitle.setText("ثبت درآمد یا مصرف جدید"); formTitle.setTextSize(19); formTitle.setTextColor(Color.rgb(25,45,70)); formTitle.setPadding(0,16,0,6); root.addView(formTitle);
    type=new Spinner(this); type.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"مصرف","درآمد"})); root.addView(type);
    amount=new EditText(this); amount.setHint("مبلغ (مثلاً 250)"); amount.setInputType(8194); root.addView(amount);
    currency=new Spinner(this); currency.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,currencies)); root.addView(currency);
    category=new Spinner(this); category.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories)); root.addView(category);
    source=new EditText(this); source.setHint("منبع درآمد / فروشگاه (اختیاری)"); root.addView(source);
    note=new EditText(this); note.setHint("توضیح (مثلاً نان و سبزی)"); root.addView(note);
    Button add=new Button(this); add.setText("ثبت اطلاعات"); root.addView(add); add.setOnClickListener(v->addEntry());
    Button report=new Button(this); report.setText("گزارش امروز و این ماه"); root.addView(report); report.setOnClickListener(v->showReport());
    Button backup=new Button(this); backup.setText("نمایش نسخه پشتیبان متنی"); root.addView(backup); backup.setOnClickListener(v->showBackup());
    TextView listTitle=new TextView(this); listTitle.setText("آخرین ثبت‌ها"); listTitle.setTextSize(19); listTitle.setTextColor(Color.rgb(25,45,70)); listTitle.setPadding(0,12,0,4); root.addView(listTitle);
    ScrollView sc=new ScrollView(this); body=new LinearLayout(this); body.setOrientation(1); sc.addView(body); root.addView(sc,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
  }
  String today(){return new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());}
  void addEntry(){try{double a=Double.parseDouble(amount.getText().toString().trim()); if(a<=0)throw new Exception(); JSONObject o=new JSONObject(); o.put("id",System.currentTimeMillis()); o.put("type",type.getSelectedItemPosition()==0?"expense":"income"); o.put("amount",a); o.put("currency",currency.getSelectedItemPosition()==0?"AFN":"USD"); o.put("category",category.getSelectedItem().toString()); o.put("source",source.getText().toString().trim()); o.put("note",note.getText().toString().trim()); o.put("date",today()); entries.put(o); save(); amount.setText("");source.setText("");note.setText(""); render(); Toast.makeText(this,"ثبت شد",Toast.LENGTH_SHORT).show();}catch(Exception e){new AlertDialog.Builder(this).setMessage("لطفاً مبلغ معتبر و بزرگ‌تر از صفر وارد کن.").setPositiveButton("باشه",null).show();}}
  void load(){try{entries=new JSONArray(prefs.getString("entries","[]"));}catch(Exception e){entries=new JSONArray();}}
  void save(){prefs.edit().putString("entries",entries.toString()).apply();}
  double sum(String type,String cur,String datePrefix){double s=0;for(int i=0;i<entries.length();i++){JSONObject o=entries.optJSONObject(i);if(o==null)continue;if(type!=null&&!type.equals(o.optString("type")))continue;if(cur!=null&&!cur.equals(o.optString("currency")))continue;if(datePrefix!=null&&!o.optString("date").startsWith(datePrefix))continue;s+=o.optDouble("amount");}return s;}
  String money(double v){return String.format(Locale.US,"%,.2f",v);}
  void render(){if(summary==null)return;String month=new SimpleDateFormat("yyyy-MM",Locale.US).format(new Date());summary.setText("این ماه\nدرآمد:  "+money(sum("income","AFN",month))+" افغانی  |  "+money(sum("income","USD",month))+" دالر\nمصرف:  "+money(sum("expense","AFN",month))+" افغانی  |  "+money(sum("expense","USD",month))+" دالر\nباقی‌مانده خالص:  "+money(sum(null,"AFN",month)==0?0:sum("income","AFN",month)-sum("expense","AFN",month))+" افغانی  |  "+money(sum("income","USD",month)-sum("expense","USD",month))+" دالر");body.removeAllViews();for(int i=entries.length()-1;i>=0;i--){JSONObject o=entries.optJSONObject(i);if(o==null)continue;String kind=o.optString("type").equals("income")?"درآمد":"مصرف";TextView t=new TextView(this);t.setText(kind+" • "+money(o.optDouble("amount"))+" "+o.optString("currency")+"\n"+o.optString("date")+" • "+o.optString("category")+"\n"+o.optString("source")+"  "+o.optString("note"));t.setTextSize(15);t.setTextColor(Color.rgb(35,45,55));t.setPadding(12,12,12,12);t.setBackgroundColor(Color.WHITE);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,4,0,4);body.addView(t,lp);final int index=i;t.setOnLongClickListener(v->{new AlertDialog.Builder(this).setTitle("حذف ثبت؟").setMessage("این مورد حذف شود؟").setNegativeButton("لغو",null).setPositiveButton("حذف",(d,w)->{removeAt(index);}).show();return true;});}}
  void removeAt(int idx){JSONArray n=new JSONArray();for(int j=0;j<entries.length();j++)if(j!=idx)n.put(entries.opt(j));entries=n;save();render();}
  void showReport(){String day=today(), month=day.substring(0,7);String s="گزارش امروز ("+day+")\n"+"درآمد AFN: "+money(sum("income","AFN",day))+"\nمصرف AFN: "+money(sum("expense","AFN",day))+"\nدرآمد USD: "+money(sum("income","USD",day))+"\nمصرف USD: "+money(sum("expense","USD",day))+"\n\nگزارش ماه («"+month+"»)\nدرآمد AFN: "+money(sum("income","AFN",month))+"\nمصرف AFN: "+money(sum("expense","AFN",month))+"\nخالص AFN: "+money(sum("income","AFN",month)-sum("expense","AFN",month))+"\nدرآمد USD: "+money(sum("income","USD",month))+"\nمصرف USD: "+money(sum("expense","USD",month))+"\nخالص USD: "+money(sum("income","USD",month)-sum("expense","USD",month));new AlertDialog.Builder(this).setTitle("گزارش مالی").setMessage(s).setPositiveButton("باشه",null).show();}
  void showBackup(){String data=entries.toString();android.widget.ScrollView s=new ScrollView(this);TextView t=new TextView(this);t.setText(data);t.setTextIsSelectable(true);t.setPadding(16,16,16,16);s.addView(t);new AlertDialog.Builder(this).setTitle("نسخه متنی اطلاعات").setMessage("برای کپی کردن متن، روی متن فشار طولانی بده. این نسخه را در جای امن نگه دار.").setView(s).setPositiveButton("بستن",null).show();}
}
