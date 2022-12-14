package com.isang.api.service;

import com.isang.api.domain.Post;
import com.isang.api.domain.PostEditor;
import com.isang.common.exception.custom.PostNotFound;
import com.isang.api.repository.PostRepository;
import com.isang.api.entity.request.PostCreate;
import com.isang.api.entity.request.PostEdit;
import com.isang.api.entity.request.PostSearch;
import com.isang.api.entity.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public void write(PostCreate postCreate){
        Post post =Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
       Post post = postRepository.findById(id)
               .orElseThrow(PostNotFound::new);

       return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }


    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
