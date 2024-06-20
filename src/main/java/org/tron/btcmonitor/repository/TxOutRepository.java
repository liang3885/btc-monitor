package org.tron.btcmonitor.repository;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tron.btcmonitor.entity.TxOut;

import java.util.List;

public interface TxOutRepository extends JpaRepository<TxOut, Long> {

    @Query(value="select * from tx_out where spent = false", nativeQuery = true)
    List<TxOut> getAllUnspentTxs();

    @Modifying
    @Query(value = "update tx_out set spent = true where tx_id = ?1 and out_index = ?2", nativeQuery = true)
    void spent(String txId, Integer outIndex);


    @Query(value = "select * from tx_out where tx_id = ?1 and out_index = ?2", nativeQuery = true)
    TxOut findByTxId(String txId, Integer outIndex);

}
