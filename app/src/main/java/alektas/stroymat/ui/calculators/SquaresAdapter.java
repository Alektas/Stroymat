package alektas.stroymat.ui.calculators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.utils.StringUtils;

public class SquaresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String SQUARE_TYPE_WALL = "SQUARE_TYPE_WALL";
    public static final String SQUARE_TYPE_WINDOW = "SQUARE_TYPE_WINDOW";
    public static final String SQUARE_TYPE_FRONTON = "SQUARE_TYPE_FRONTON";
    public static final String SQUARE_TYPE_ROOF = "SQUARE_TYPE_ROOF";
    public static final String SQUARE_TYPE_TROTUAR = "SQUARE_TYPE_TROTUAR";
    private SquareViewModelBase mModel;
    private List<Square> mSquares;
    private String mSquareType;

    public SquaresAdapter(SquareViewModelBase model, String squareType) {
        mModel = model;
        mSquares = new ArrayList<>();
        mSquareType = squareType;
        setHasStableIds(true);
    }

    public static class SquareHolder extends RecyclerView.ViewHolder {
        View view;
        TextView heightText;
        TextView widthText;
        TextView squareText;

        private SquareHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            heightText = itemView.findViewById(R.id.square_height);
            widthText = itemView.findViewById(R.id.square_width);
            squareText = itemView.findViewById(R.id.result_square);
        }
    }

    @Override
    public int getItemCount() {
        if (mSquares == null) return 0;
        return mSquares.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_square, parent, false);
        return new SquareHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final Square square = mSquares.get(position);
        SquareHolder vh = (SquareHolder) viewHolder;
        vh.heightText.setText(StringUtils.format(square.getHeight()));
        vh.widthText.setText(StringUtils.format(square.getWidth()));
        vh.squareText.setText(StringUtils.format(square.getSquare()));
        vh.view.findViewById(R.id.item_del_btn).setOnClickListener(v -> {
            mModel.removeSquare(square, mSquareType);
        });
    }

    public List<Square> getSquares() {
        return mSquares;
    }

    public void addSquare(Square square){
        mSquares.add(square);
        notifyDataSetChanged();
    }

    public void setSquares(List<Square> newSquares) {
        mSquares.clear();
        mSquares.addAll(newSquares);
        notifyDataSetChanged();
    }
}
