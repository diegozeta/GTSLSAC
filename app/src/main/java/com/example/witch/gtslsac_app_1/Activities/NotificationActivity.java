package com.example.witch.gtslsac_app_1.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacion;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacionCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacionCollection;
import com.example.witch.gtslsac_app_1.mVolley.OperacionesVolley;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {
    private static final String NOMBRE_CARPETA_APP = "com.example.witch.gtslsac_app_1";
    private static final String GENERADOS = "MisArchivos";
    public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
    public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);

    private PdfWriter pdfWriter;
    private Font font;
    private ArrayList<ReporteNotificacion> reporteNotificaciones;
    private OperacionesVolley operacionesVolley = new OperacionesVolley();
    ReporteNotificacionCRUD reporteNotificacionCRUD = new ReporteNotificacionCRUD(ReporteNotificacionCollection.getReporteNotificaciones());
    Button btnVerReporteNotificacion;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    private String id = "0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id = extras.getString("id_alquiler");
            operacionesVolley.cargarDatosReporteNotificacionRecyclerView(NotificationActivity.this, id);
            reporteNotificaciones = new ArrayList<>();
            reporteNotificaciones = reporteNotificacionCRUD.getDetalleAlquileres();
            // and get whatever type user account id is
        }
        Log.e("ID_RECIBIDO FIREBASE", id + " ---");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (CheckPermission(NotificationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // you have permission go ahead
            createApplicationFolder();
        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(NotificationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_RUNTIME_PERMISSION);
        }
        btnVerReporteNotificacion = (Button) findViewById(R.id.btn_ver_reporte_notificacion);

        btnVerReporteNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF(NotificationActivity.this, "");
                //reporteNotificacionCRUD.deleteAll();
            }
        });
    }

    private void createApplicationFolder() {
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }
        Log.e("DIRECTORIOS CREADOS?", "DIRECTORIO: " + String.valueOf(pdfDir.exists()) + " SUBDIRECTORIO : " + String.valueOf(pdfSubDir.exists()));
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {
            case REQUEST_RUNTIME_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // you have permission go ahead
                    createApplicationFolder();
                } else {
                    // you do not have permission show toast.
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission)) {
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Permission},
                        Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createPDF(Context context, String reportName) {
        Document document = new Document(PageSize.LETTER);
        String NOMBRE_ARCHIVO = "MiArchivo.pdf";
        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP + File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;
        File outputfile = new File(nombre_completo);
        if (outputfile.exists()) {
            outputfile.delete();
        }
        try {
            //assigning a PdfWriter instance to pdfWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(nombre_completo));

            //PageFooter is an inner class of this class which is responsible to create Header and Footer
            NotificationActivity.PageHeaderFooter event = new NotificationActivity.PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();
            try {
                // get input stream
                InputStream ims = NotificationActivity.this.getAssets().open("logo_b.gif");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(100, 80);
                document.add(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            //Adding meta-data to the document
            addMetaData(document);
            //Adding Title(s) of the document
            addTitlePage(document);
            //Adding main contents of the document
            addContent(document);
            //Closing the document
            document.close();
            muestraPdf(nombre_completo, NotificationActivity.this);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    public void muestraPdf(String archivo, Context context) {
        Toast.makeText(context, "LEYENDO EL ARCHIVO", Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        //para limpiar el caché de la aplicacion y abrir un nuevo pdf
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "NO EXISTE NINGUN PROGRAMA PARA LEER ESTE TIPO DE ARCHIVOS (pdf)", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
     * on the file and to to properties then you can see all these information.
     *
     * @param document
     */
    private static void addMetaData(Document document) {
        document.addTitle("DETALLE ALQUILERES");
        document.addSubject("none");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("GTSLSAC");
        document.addCreator("WITCHRAPER");
    }

    /**
     * In this method title(s) of the document is added.
     *
     * @throws DocumentException
     */
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph("DETALLE DE ALQUILER", FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        /*childParagraph = new Paragraph("LISTA DE DETALLE DE ALQUILER", FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);*/

        Calendar myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date fechaSeleccionada = new Date();
        String fechaFormateada="";
        String fecha = sdf.format(myCalendar.getTime());
        try {
            fechaSeleccionada = sdf.parse(fecha);
            fechaFormateada = sdf.format(fechaSeleccionada);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //+ year + "/" + month + "/" + day
        childParagraph = new Paragraph("REPORTE GENERADO EL: " + fechaFormateada, FONT_SUBTITLE);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        addEmptyLine(paragraph, 2);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        //End of adding several titles

    }

    /**
     * In this method the main contents of the documents are added
     *
     * @param document
     * @throws DocumentException
     */

    private void addContent(Document document) throws DocumentException {

        Paragraph reportBody = new Paragraph();
        reportBody.setFont(FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);

        // Creating a table
        createTable(reportBody);

        // now add all this to the document
        document.add(reportBody);

    }

    private void createTable(Paragraph reportBody)
            throws BadElementException {

        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.addCell(createCell("ALQUILER Nro", 2, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(String.valueOf(reporteNotificaciones.get(0).getIdAlquiler()), 8, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell("USUARIO", 2, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(reporteNotificaciones.get(0).getNombresUsuario() + " " + reporteNotificaciones.get(0).getApellidosUsuario(), 8, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell("CLIENTE", 2, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(reporteNotificaciones.get(0).getNombreEmpresa(), 8, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell("FECHA INICIO", 2, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(reporteNotificaciones.get(0).getFechaInicio(), 3, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell("FECHA FIN", 2, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(reporteNotificaciones.get(0).getFechaFin(), 3, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell(" ", 10, 1, PdfPCell.TOP, "contenido"));



        for (int i = 0; i < reporteNotificaciones.size(); i++) { //
            table.addCell(createCell("DETALLE Nro", 2, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getIdDetalleAlquiler()), 8, 1, PdfPCell.BOX, "contenido"));

            table.addCell(createCell("EQUIPO", 5, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("TRACTO/CAMA", 5, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("NOMBRE", 1, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("MARCA", 1, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("MODELO", 2, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("CODIGO", 1, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("NOMBRE", 1, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("MARCA", 1, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("MODELO", 2, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("CODIGO", 1, 1, PdfPCell.BOX, "titulo"));

            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getNombreEquipo1()), 1, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getMarcaEquipo1()), 1, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getModeloEquipo1()), 2, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getCodigoEquipo1()), 1, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getNombreEquipo2()), 1, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getMarcaEquipo2()), 1, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getModeloEquipo2()), 2, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getCodigoEquipo2()), 1, 1, PdfPCell.BOX, "contenido"));

            table.addCell(createCell("OPERADOR", 5, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("RIGGER/AYUDANTE", 5, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("NOMBRES", 2, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("APELLIDOS", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("NOMBRES", 2, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell("APELLIDOS", 3, 1, PdfPCell.BOX, "titulo"));

            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getNombresOperador1()), 2, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getApellidosOperador1()), 3, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getNombresOperador2()), 2, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getApellidosOperador2()), 3, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell(" ", 10, 1, PdfPCell.TOP, "contenido"));
        }

        reportBody.add(table);
    }

    public PdfPCell createCell(String content, int colspan, int rowspan, int border, String opcion) {
        if (opcion.equals("titulo")) {
            font = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);

        } else {
            font = new Font(Font.FontFamily.HELVETICA, 10);
        }
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        if (opcion.equals("titulo")) {
            cell.setBackgroundColor(new GrayColor(0.75f));
        }

        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorder(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    class PageHeaderFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {

            /**
             * PdfContentByte is an object containing the user positioned text and graphic contents
             * of a page. It knows how to apply the proper font encoding.
             */
            PdfContentByte cb = writer.getDirectContent();

            /**
             * In iText a Phrase is a series of Chunks.
             * A chunk is the smallest significant part of text that can be added to a document.
             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
             */
            Phrase header = new Phrase("GRUAS Y TRANSPORTES SAN LORENZO S.A.C.", FONT_HEADER_FOOTER);
            Phrase footer_poweredBy = new Phrase("GRUAS Y TRANSPORTES SAN LORENZO S.A.C.", FONT_HEADER_FOOTER); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
            Phrase footer_pageNumber = new Phrase("PAGINA " + document.getPageNumber(), FONT_HEADER_FOOTER);

            // Header
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
                    (document.getPageSize().getWidth() - 10),
                    document.top() + 10, 0);

            // footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }
}
