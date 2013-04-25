
sdir = dir('S*');
wdir = dir('W*');
mkdir('_small');
for i=1:length(sdir),
    dirname = sdir(i).name;
    mkdir('_small',dirname);
    filelist = dir([dirname '/*.jpg']);
    for j=1:length(filelist),
        imname = [dirname '/' filelist(j).name]
        image = imread(imname);
        resizedimage = imresize(image, 150/size(image,1));
        imwrite(resizedimage, ['_small/' imname],'JPEG','Quality',100);
    end
end
for i=1:length(wdir),
    dirname = wdir(i).name;
    mkdir('_small',dirname);
    filelist = dir([dirname '/*.jpg']);
    for j=1:length(filelist),
        imname = [dirname '/' filelist(j).name]
        image = imread(imname);
        resizedimage = imresize(image, 150/size(image,1));
        imwrite(resizedimage, ['_small/' imname],'JPEG','Quality',100);
    end
end