package org.tron.btcmonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tron.btcmonitor.entity.Progress;
import org.tron.btcmonitor.repository.ProgressRepository;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    public Long getValue(String key) {
        Progress progress =  progressRepository.getByKey(key);
        if (progress == null) {
            return null;
        }
        return progress.getValue();
    }

    public void save(String progressKey, Long lastHeight) {
        Progress progress =  progressRepository.getByKey(progressKey);
        if (progress == null) {
            Progress progress2 = new Progress();
            progress2.setValue(lastHeight);
            progress2.setProgressKey(progressKey);
            progressRepository.save(progress2);
            return;
        }
        progressRepository.update(progressKey, lastHeight);
    }
}
