package net.ahmetkilinc.downloadfileexample;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {

    //indirilecek dosyamızın url'sini tutacak String
    private String indirilecekDosyaYolu;
    //ön izlememizi gösterecek ImageView
    private ImageView ivIndirmedenOnceGoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml dosyamızda tanımladığımız button ve imageview'i tanımladık.
        ivIndirmedenOnceGoster = findViewById(R.id.imageViewKratos);
        Button btnDosyayiIndir = findViewById(R.id.buttonIndir);

        //String'e url yi yazıyoruz. Burada siz uygulamanıza göre gereken url'yi atayacaksınız.
        indirilecekDosyaYolu = "https://ahmetkilinc.net/wp-content/uploads/2018/08/kratos-emoji.jpeg";

        //Glide kütüphanesini kullanarak url deki jpeg dosyasını imageview de gösterdik.
        Glide.with(this).load(indirilecekDosyaYolu).into(ivIndirmedenOnceGoster);

        //uygulama ilk açıldığında dosyayı telefona kaydetmek için izin alacağımız fonksiyon.
        haveStoragePermission();

        //indir butonuna tıklandığında olacaklar.
        btnDosyayiIndir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = indirilecekDosyaYolu;
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("God of War");
                request.setTitle("KRATOOOS");

                //burada çok çok eski bir sürüm için gerekli bir denetleme yapıyoruz. bu kısmı kaldırabilirsiniz.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }

                //dosyayı yazacağımız yer ve dosyanın ismine karar verebiliyoruz.
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "kratos.jpeg");

                //download servisini çalıştırma ve kuyruğa alması
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });
    }

    //kullanıcıdan yazma işi için izin alma fonksiyonumuz.
    public  boolean haveStoragePermission() {

        //izin alma işlemi api level 23'den sonra geldiği için onu kontrol ediyoruz.
        if (Build.VERSION.SDK_INT >= 23) {

            //manifestteki izin kontrol ediliyor.
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }

        // kullanıcı 23'den eski bir sistem kullanıyorsa izin alma işlemi yapılmadan devam ediliyor.
        else {
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }
}
