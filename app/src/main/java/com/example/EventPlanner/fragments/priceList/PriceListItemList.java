package com.example.EventPlanner.fragments.priceList;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.adapters.priceList.PriceListAdapter;
import com.example.EventPlanner.databinding.FragmentPriceListBinding;
import com.example.EventPlanner.model.priceList.PriceListItem;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceListItemList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceListItemList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPriceListBinding priceListBinding;
    private PriceListViewModel priceListViewModel;

    public PriceListItemList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceList.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceListItemList newInstance(String param1, String param2) {
        PriceListItemList fragment = new PriceListItemList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        priceListBinding = FragmentPriceListBinding.inflate(getLayoutInflater());
        priceListViewModel = new ViewModelProvider(requireActivity()).get(PriceListViewModel.class);
        RecyclerView recyclerView = priceListBinding.priceListItemListVertical;

        priceListViewModel.getPriceListItems().observe(getViewLifecycleOwner(), priceList -> {
            priceListBinding.generatePdfButton.setOnClickListener(v -> generatePDF(priceList));
            PriceListAdapter adapter = new PriceListAdapter(requireContext(), priceList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        priceListViewModel.getPriceList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // Inflate the layout for this fragment
        return priceListBinding.getRoot();
    }

    private void generatePDF(ArrayList<PriceListItem> priceList) {
        String downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filePath = downloadsDirectory + "/PriceListReport.pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            float[] columnWidths = {6, 4, 4, 4};
            Table table = new Table(columnWidths);

            table.addCell(new Cell().add(new Paragraph("Title").setBold()));
            table.addCell(new Cell().add(new Paragraph("Price").setBold()));
            table.addCell(new Cell().add(new Paragraph("Discount").setBold()));
            table.addCell(new Cell().add(new Paragraph("Total").setBold()));

            for(PriceListItem priceListItem: priceList) {
                table.addCell(priceListItem.getTitle());
                table.addCell(priceListItem.getPrice() + "€");
                table.addCell(priceListItem.getDiscount() + "%");
                table.addCell(priceListItem.getDiscountedPrice() + "€");
            }

            document.add(table);
            document.close();
            openPDF(filePath);
        }catch (Exception e) {
            Log.e("PDF Generation", "Error creating PDF: " + e.getMessage());
        }
    }

    private void openPDF(String filePath) {
        File pdfFile = new File(filePath);
        Uri pdfUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(requireContext(), "com.example.EventPlanner.fileprovider", pdfFile);
        }else {
            pdfUri = Uri.fromFile(pdfFile);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }
}