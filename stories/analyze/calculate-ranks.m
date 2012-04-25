
for id = all_labels 
  story_name = strcat('story_',num2str(id));
  crt_matrix = eval(story_name);

  [vects,vals] = eig(crt_matrix);
  [vmax, i] = max(abs([vals(1,1), vals(2,2), vals(3,3)])) ; 
  #ranking = abs(vects(:,i));
  ranking = vects(:,i); 

  ranking = ranking / sum(ranking);
  printf("%d ; %f ; %f ; %f\n", id, ranking);
end
