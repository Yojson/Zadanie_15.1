package com.example.zadanie_151;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final Context context;
    private final List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.bind_note(note);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Defensive checks
                if (note == null) return;
                // Cast context to MainActivity and call delete_note
                if (context instanceof MainActivity) {
                    ((MainActivity) context).delete_note(note.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_note_id;
        private final TextView tv_note_content;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_note_id = itemView.findViewById(R.id.tv_note_id);
            tv_note_content = itemView.findViewById(R.id.tv_note_content);
        }

        public void bind_note(Note note) {
            if (note == null) {
                tv_note_id.setText("ID: -");
                tv_note_content.setText("");
                return;
            }
            tv_note_id.setText("ID: " + note.getId());
            String content = (note.getTitle() == null ? "" : note.getTitle() + " — ")
                    + (note.getContent() == null ? "" : note.getContent());
            tv_note_content.setText(content);
        }
    }
}
