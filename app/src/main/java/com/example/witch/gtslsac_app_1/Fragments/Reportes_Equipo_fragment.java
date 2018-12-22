package com.example.witch.gtslsac_app_1.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacion;
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

public class Reportes_Equipo_fragment extends Fragment {
    private static final String NOMBRE_CARPETA_APP = "com.example.witch.gtslsac_app_1";
    private static final String GENERADOS = "MisArchivos";
    public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
    public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
    private PdfWriter pdfWriter;
    private Font font;
    private ArrayList<ReporteNotificacion> reporteNotificaciones;
    private OperacionesVolley operacionesVolley = new OperacionesVolley();
    Button btnGenerarReporte, btnVerReporte;
    EquiposCRUD equiposCRUD = new EquiposCRUD(EquiposCollection.getEquipos());
    ArrayList<Equipo> equipos;
    CheckBox chkPeriodoReporteEquipo;
    TextView txtFechaDesde, txtFechaHasta;
    AutoCompleteTextView autoCompleteTextViewReporteEquipo;
    ArrayAdapter<Equipo> adaptadorEquipos;
    int idEquipo = 0;
    String nombreEquipo;
    String opcionFecha;
    String sqlDateInicio, sqlDateFin;

    private static final int REQUEST_RUNTIME_PERMISSION = 123;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (CheckPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // you have permission go ahead
            createApplicationFolder();
        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_RUNTIME_PERMISSION);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("REP_EQUIPOS ONCREATE", "LLAMADO!!");
        // Inflate the layout for this fragment
        final View myView = inflater.inflate(R.layout.reportes_equipo_fragment, container, false);
        btnGenerarReporte = (Button) myView.findViewById(R.id.btn_generar_reporte_equipo);
        btnVerReporte = (Button) myView.findViewById(R.id.btn_ver_reporte_equipo);

        txtFechaDesde = (TextView) myView.findViewById(R.id.txt_desde_reporte_equipo);
        txtFechaHasta = (TextView) myView.findViewById(R.id.txt_hasta_reporte_equipo);

        autoCompleteTextViewReporteEquipo = (AutoCompleteTextView) myView.findViewById(R.id.autocomplete_reporte_equipo);
        autoCompleteTextViewReporteEquipo.setThreshold(1);
        adaptadorEquipos = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, operacionesVolley.equipos);
        autoCompleteTextViewReporteEquipo.setAdapter(adaptadorEquipos);

        equipos = new ArrayList<>();
        equipos = equiposCRUD.getEquipos();

        operacionesVolley.cargarDatosEquiposRecyclerView(getContext());

        btnGenerarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DATOS ENVIADOS", "IDEQUIPO:>>>" + idEquipo + " DESDE " + sqlDateInicio + " HASTA " + sqlDateFin);
                if (idEquipo == 0) {
                    Toast.makeText(getContext(), "SELECCIONE UN EQUIPO!", Toast.LENGTH_LONG).show();
                } else {
                    if ((txtFechaDesde.getText().equals("DESDE")) || (txtFechaHasta.getText().equals("HASTA"))) {
                        Toast.makeText(getContext(), "SELECCIONE UN PERIODO!", Toast.LENGTH_LONG).show();
                    } else {
                        operacionesVolley.cargarDatosReporteEquiposRecyclerView(getContext(), String.valueOf(idEquipo), sqlDateInicio, sqlDateFin);
                        btnGenerarReporte.setVisibility(View.GONE);
                        btnVerReporte.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "REPORTE GENERADO!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnVerReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporteNotificaciones = new ArrayList<>();
                reporteNotificaciones = operacionesVolley.reporteNotificaciones;
                createPDF(getContext(), "");
            }
        });

        autoCompleteTextViewReporteEquipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Equipo) {
                    Equipo equipo = (Equipo) item;
                    idEquipo = equipo.getIdEquipo();
                    nombreEquipo = equipo.getNombreEquipo() + " " + equipo.getMarcaEquipo()+ " " + equipo.getModeloEquipo()+ " " + equipo.getMarcaEquipo() + " " + equipo.getCodigoEquipo();
                }
                Log.e("ID EQUIPO", String.valueOf(idEquipo));
                Log.e("HAY ", String.valueOf(operacionesVolley.equipos.size()) + " EQUIPOS");
                autoCompleteTextViewReporteEquipo.setText(nombreEquipo);
                autoCompleteTextViewReporteEquipo.setFocusable(false);
            }
        });


        txtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionFecha = "Inicio";
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        txtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionFecha = "Fin";
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return myView;
    }

    public static int restarFecha(Date fechaMayor, Date fechaMenor) {
        long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
        long horas = diferenciaEn_ms / (1000 * 60 * 60);
        return (int) horas;
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            colocarFecha(opcionFecha);
        }

    };

    private void colocarFecha(String opcion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechaSeleccionada = new Date();
        if (opcion.equals("Inicio")) {
            txtFechaDesde.setText(sdf.format(myCalendar.getTime()));
            try {
                fechaSeleccionada = sdf.parse(txtFechaDesde.getText().toString());
                sqlDateInicio = tdf.format(fechaSeleccionada);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            txtFechaHasta.setText(sdf.format(myCalendar.getTime()));
            try {
                fechaSeleccionada = sdf.parse(txtFechaHasta.getText().toString());
                sqlDateFin = tdf.format(fechaSeleccionada);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ESTADO DE ONRESUME: ", "ONRESUME LLAMADO!!");
        //Para recargar el formulario y poder hacer un nuevo reporte
        if (idEquipo == 0) {

        } else {
            idEquipo = 0;
            Reportes_Equipo_fragment reportes_equipo_fragment = new Reportes_Equipo_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, reportes_equipo_fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
            PageHeaderFooter event = new PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();
            try {
                // get input stream
                InputStream ims = getContext().getAssets().open("logo_b.gif");
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
            muestraPdf(nombre_completo, getContext());
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
        document.addTitle("REPORTE EQUIPOS");
        document.addSubject("Reporte Equipos");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("GTSLSAC");
        document.addCreator("GTSLSAC");
    }

    /**
     * In this method title(s) of the document is added.
     *
     * @param document
     * @throws DocumentException
     */
    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph("REPORTE EQUIPO:\n" + autoCompleteTextViewReporteEquipo.getText().toString(), FONT_TITLE);
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
        String fecha = sdf.format(myCalendar.getTime());
        try {
            fechaSeleccionada = sdf.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //+ year + "/" + month + "/" + day
        childParagraph = new Paragraph("PERIODO\nDESDE: " + txtFechaDesde.getText().toString() + " HASTA: " + txtFechaHasta.getText().toString(), FONT_SUBTITLE);
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
        Log.e("TAMAÑO REP_EQUIPOS", String.valueOf(reporteNotificaciones.size()));
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(90);

        table.addCell(createCell("TOTAL ALQUILERES", 3, 1, PdfPCell.BOX, "titulo"));
        table.addCell(createCell(String.valueOf(reporteNotificaciones.size()), 1, 1, PdfPCell.BOX, "contenido"));
        table.addCell(createCell(" ", 3, 1, PdfPCell.NO_BORDER, "contenido"));
        table.addCell(createCell(" ", 7, 1, PdfPCell.NO_BORDER, "contenido"));
        table.addCell(createCell(" ", 7, 1, PdfPCell.NO_BORDER, "contenido"));

        for (int i = 0; i < reporteNotificaciones.size(); i++) { //
            table.addCell(createCell("ALQUILER Nro", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(String.valueOf(reporteNotificaciones.get(i).getIdAlquiler()), 4, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell("REGISTRADO POR USUARIO", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getNombresUsuario() + " " + reporteNotificaciones.get(i).getApellidosUsuario(), 7, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell("SERVICIO A CLIENTE", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getNombreEmpresa(), 4, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell("FECHA INICIO ALQUILER", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getFechaInicio(), 4, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell("FECHA FIN ALQUILER", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getFechaFin(), 4, 1, PdfPCell.BOX, "contenido"));
            //table.addCell(createCell(" ", 10, 1, PdfPCell.TOP, "contenido"));

            table.addCell(createCell("EQUIPO", 3, 1, PdfPCell.BOX, "titulo"));
            String Equipo = reporteNotificaciones.get(i).getNombreEquipo1() + " " + reporteNotificaciones.get(i).getMarcaEquipo1() + " " + reporteNotificaciones.get(i).getModeloEquipo1() + " " + reporteNotificaciones.get(i).getCodigoEquipo1();
            table.addCell(createCell(Equipo, 4, 1, PdfPCell.BOX, "contenido"));

            table.addCell(createCell("TRACTO/CAMA", 3, 1, PdfPCell.BOX, "titulo"));
            String Tracto = reporteNotificaciones.get(i).getNombreEquipo2() + " " + reporteNotificaciones.get(i).getMarcaEquipo2() + " " + reporteNotificaciones.get(i).getModeloEquipo2() + " " + reporteNotificaciones.get(i).getCodigoEquipo2();
            table.addCell(createCell(Tracto, 4, 1, PdfPCell.BOX, "contenido"));

            table.addCell(createCell("OPERADOR", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getNombresOperador1() + " " + reporteNotificaciones.get(i).getApellidosOperador1(), 4, 1, PdfPCell.BOX, "contenido"));
            table.addCell(createCell("RIGGER/AYUDANTE", 3, 1, PdfPCell.BOX, "titulo"));
            table.addCell(createCell(reporteNotificaciones.get(i).getNombresOperador2() + " " + reporteNotificaciones.get(i).getApellidosOperador2(), 4, 1, PdfPCell.BOX, "contenido"));

            table.addCell(createCell(" ", 7, 1, PdfPCell.NO_BORDER, "contenido"));
            table.addCell(createCell(" ", 7, 1, PdfPCell.NO_BORDER, "contenido"));
        }

        reportBody.add(table);
    }

    public PdfPCell createCell(String content, int colspan, int rowspan, int border, String opcion) {
        if (opcion.equals("titulo")) {
            font = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            //font.setColor(BaseColor.WHITE);
        } else {
            font = new Font(Font.FontFamily.HELVETICA, 10);
        }
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        if (opcion.equals("titulo")) {
            //BaseColor myColor = WebColors.getRGBColor("#147fbb");
            //cell.setBackgroundColor(myColor);
            cell.setBackgroundColor(new GrayColor(0.75f));
        }

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorder(border);
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



