package com.example.meuqrcode.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuqrcode.databinding.ScannedItemBinding;
import com.example.meuqrcode.domain.QRLido;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QRItemAdapter extends RecyclerView.Adapter<QRItemAdapter.ViewHolder>{
    private List<QRLido> qrLidoList;

    public QRItemAdapter() {
        qrLidoList = new ArrayList<>();

    }

    public List<QRLido> getQrLidoList(){return qrLidoList;}

    @NonNull
    @Override
    public QRItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ScannedItemBinding binding = ScannedItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QRItemAdapter.ViewHolder holder, int position) {
        QRLido qr = qrLidoList.get(position);
        holder.binding.tvQRCode.setText(qr.getCode());
        holder.binding.tvQRDateReaded.setText(qr.getDate().toString());

        holder.binding.ibDelete.setOnClickListener(view -> deleteItem(position));
    }

    private void deleteItem(int position) {
        qrLidoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, getItemCount());
    }

    @Override
    public int getItemCount() {return qrLidoList.size();}

    public void add_qr(@NotNull QRLido qr) {
        qrLidoList.add(qr);
        notifyItemInserted(getItemCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ScannedItemBinding binding;

        public ViewHolder(ScannedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
