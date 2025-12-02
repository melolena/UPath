package com.example.upath;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upath.R;

import java.util.List;

public class MembroAdapter extends RecyclerView.Adapter<MembroAdapter.ViewHolder> {

    private Context context;
    private List<Membro> lista;

    public MembroAdapter(Context context, List<Membro> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_membro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membro m = lista.get(position);
        holder.nome.setText(m.getNome());
        holder.cargo.setText(m.getCargo());
        holder.foto.setImageResource(m.getFoto());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome, cargo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imgFoto);
            nome = itemView.findViewById(R.id.txtNome);
            cargo = itemView.findViewById(R.id.txtCargo);
        }
    }
}
