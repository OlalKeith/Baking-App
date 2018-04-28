package com.example.vidbregar.bakingapp.ui.recipe_step;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment implements Player.EventListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private Context context;
    private RecipeStep recipeStep;
    private SimpleExoPlayer simpleExoPlayer;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private boolean isPlaying;
    private long currentPlayerPosition;

    @BindView(R.id.recipe_step_player_view)
    PlayerView recipeStepPlayerView;
    @BindView(R.id.recipe_step_short_description_tv)
    TextView recipeStepTitleTextView;
    @BindView(R.id.recipe_step_description_tv)
    TextView recipeStepDescriptionTextView;
    @Nullable // Available only in landscape mode
    @BindView(R.id.recipe_step_container_frame_layout)
    FrameLayout recipeStepContainer;
    @Nullable // Available only in landscape mode
    @BindView(R.id.recipe_instructions_container)
    ScrollView recipeInstructionsContainer;
    @BindView(R.id.video_loading_progress_bar)
    ProgressBar videoLoadingProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            recipeStep = args.getParcelable(RecipeStepActivity.RECIPE_STEP_ARGS_KEY);
            currentPlayerPosition = args.getLong(RecipeStepActivity.CURRENT_POSITION_ARGS_KEY);
            isPlaying = args.getBoolean(RecipeStepActivity.IS_PLAYING_ARGS_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        context = rootView.getContext();
        ButterKnife.bind(this, rootView);

        initialize();

        return rootView;
    }

    private void initialize() {
        boolean hasVideo = !recipeStep.getVideoUrl().isEmpty();
        recipeStepPlayerView.setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && hasVideo) {
            recipeStepContainer.setBackgroundColor(Color.BLACK);
            recipeInstructionsContainer.setVisibility(View.GONE);
            prepareVideoPlayback();
        } else if (hasVideo) {
            setLoadingIndicatorPosition();
            addInstructionsToViews();
            prepareVideoPlayback();
        } else {
            videoLoadingProgressBar.setVisibility(View.GONE);
            addInstructionsToViews();
        }
    }

    private void setLoadingIndicatorPosition() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;

        // calculate height of the video (1920x1080)
        int videoHeightPixels = (1080 * widthPixels) / 1920;
        ViewGroup.LayoutParams layoutParams = videoLoadingProgressBar.getLayoutParams();
        layoutParams.height = videoHeightPixels;
        videoLoadingProgressBar.setLayoutParams(layoutParams);
    }

    private void addInstructionsToViews() {
        recipeStepTitleTextView.setText(recipeStep.getShortDescription());
        recipeStepDescriptionTextView.setText(recipeStep.getDescription());
    }

    private void prepareVideoPlayback() {
        initializeMediaSession();
        initializePlayer(Uri.parse(recipeStep.getVideoUrl()));
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(context, TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(playbackStateBuilder.build());

        mediaSession.setCallback(new MediaSessionCallbacks());

        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri videoUrl) {
        if (simpleExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            recipeStepPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(context, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(context, userAgent))
                    .createMediaSource(videoUrl);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.seekTo(currentPlayerPosition);
            simpleExoPlayer.setPlayWhenReady(isPlaying);
            isPlaying = true;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (Player.STATE_READY == playbackState) {
            videoLoadingProgressBar.setVisibility(View.GONE);
            recipeStepPlayerView.setVisibility(View.VISIBLE);
            if (playWhenReady) {
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        simpleExoPlayer.getCurrentPosition(), 1f);
                isPlaying = true;
            } else {
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        simpleExoPlayer.getCurrentPosition(), 1f);
                isPlaying = false;
            }
        }

    }

    class MediaSessionCallbacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
            isPlaying = true;
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
            isPlaying = false;
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            currentPlayerPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deactivateMediaSession();
        releasePlayer();
    }

    private void deactivateMediaSession() {
        if (mediaSession != null)
            mediaSession.setActive(false);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    protected long getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    protected boolean isPlaying() {
        return isPlaying;
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    // @formatter:off
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }

    @Override
    public void onLoadingChanged(boolean isLoading) { }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) { }

    @Override
    public void onRepeatModeChanged(int repeatMode) { }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }

    @Override
    public void onPlayerError(ExoPlaybackException error) { }

    @Override
    public void onPositionDiscontinuity(int reason) { }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }

    @Override
    public void onSeekProcessed() { }
    // @formatter:on
}
